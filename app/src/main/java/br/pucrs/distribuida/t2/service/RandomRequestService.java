package br.pucrs.distribuida.t2.service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class RandomRequestService {
	
	private final NodeService nodeService;
	private final NetworkService networkService;
	private final Random random;
	
	@Inject
	public RandomRequestService(NodeService nodeService, NetworkService networkService) {
		this.nodeService = nodeService;
		this.networkService = networkService;
		random = new Random();
	}
	
	public void run() {
		while (true) {
			wait(threeToSixSeconds());
			sendToCoordinator(lockOrUnlock());
			System.out.println(receiveFromCoordinator());
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
		networkService.send(message, nodeService.getCoordinator());
	}
	
	private String lockOrUnlock() {
		return random.nextBoolean()
				? CoordinatorService.LOCK
				: CoordinatorService.UNLOCK;
	}
	
	private String receiveFromCoordinator() {
		return networkService.receive().getMessage();
	}
	
}
