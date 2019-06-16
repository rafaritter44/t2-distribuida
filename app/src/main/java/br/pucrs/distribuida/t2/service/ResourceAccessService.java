package br.pucrs.distribuida.t2.service;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ResourceAccessService {
	
	private static final String RESOURCE_IP = "172.18.0.22";
	private static final int RESOURCE_PORT = 10_000;
	private static final int INTERVAL = 1;
	
	private final ClientService clientService;
	private final NodeService nodeService;
	
	@Inject
	public ResourceAccessService(ClientService clientService, NodeService nodeService) {
		this.clientService = clientService;
		this.nodeService = nodeService;
	}
	
	public void access() {
		clientService.send(in(), RESOURCE_IP, RESOURCE_PORT);
		wait(INTERVAL);
		clientService.send(out(), RESOURCE_IP, RESOURCE_PORT);
	}
	
	private String in() {
		return String.format("in: %s", idAndInstant());
	}
	
	private String out() {
		return String.format("out: %s", idAndInstant());
	}
	
	private String idAndInstant() {
		return String.format("node %d at %s", nodeService.getSelf().getId(), Instant.now());
	}
	
	private void wait(int seconds) {
		try {
			TimeUnit.SECONDS.sleep(seconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

}
