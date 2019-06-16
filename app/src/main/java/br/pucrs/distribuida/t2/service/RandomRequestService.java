package br.pucrs.distribuida.t2.service;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import io.rsocket.AbstractRSocket;

@Singleton
public class RandomRequestService extends AbstractRSocket {
	
	private final NodeService nodeService;
	private final ClientService clientService;
	private final BullyService bullyService;
	private final Random random;
	
	@Inject
	public RandomRequestService(NodeService nodeService, ClientService clientService, BullyService bullyService) {
		this.nodeService = nodeService;
		this.clientService = clientService;
		this.bullyService = bullyService;
		random = new Random();
	}
	
	public void start() {
		while (true) {
			wait(threeToSixSeconds());
			if (!bullyService.calledElection()) {
				sendToCoordinator(lockOrUnlock(), ifCoordinatorIsOOS -> {
					System.out.println("Coordinator is oos!");
					bullyService.callElection();
				});
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
	
	private void sendToCoordinator(String message, Consumer<? super Throwable> fallback) {
		clientService.send(message, nodeService.getCoordinator(), fallback);
	}
	
	private String lockOrUnlock() {
		return random.nextBoolean()
				? CoordinatorService.LOCK
				: CoordinatorService.UNLOCK;
	}
	
}
