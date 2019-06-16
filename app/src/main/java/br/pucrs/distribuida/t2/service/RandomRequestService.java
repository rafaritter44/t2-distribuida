package br.pucrs.distribuida.t2.service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import io.rsocket.AbstractRSocket;

@Singleton
public class RandomRequestService extends AbstractRSocket {
	
	private final NodeService nodeService;
	private final ClientService clientService;
	private final Random random;
	
	@Inject
	public RandomRequestService(NodeService nodeService, ClientService clientService) {
		this.nodeService = nodeService;
		this.clientService = clientService;
		random = new Random();
	}
	
	public void start() {
		while (true) {
			wait(threeToSixSeconds());
			try {
				sendToCoordinator(lockOrUnlock());
			} catch (RuntimeException e) {
				System.out.println("Coordinator is oos!");
			}
		}
	}
	
	private void wait(int seconds) {
		try {
			TimeUnit.SECONDS.sleep(seconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	private int threeToSixSeconds() {
		return random.nextInt(4) + 3;
	}
	
	private void sendToCoordinator(String message) {
		clientService.send(message, nodeService.getCoordinator());
	}
	
	private String lockOrUnlock() {
		return random.nextBoolean()
				? CoordinatorService.LOCK
				: CoordinatorService.UNLOCK;
	}
	
}
