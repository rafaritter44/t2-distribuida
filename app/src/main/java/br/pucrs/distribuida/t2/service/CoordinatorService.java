package br.pucrs.distribuida.t2.service;

import java.util.Optional;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import br.pucrs.distribuida.t2.exception.NodeNotFoundException;
import br.pucrs.distribuida.t2.model.Node;
import io.rsocket.Payload;

@Singleton
public class CoordinatorService {
	
	public static final String LOCK = "lock";
	public static final String UNLOCK = "unlock";
	public static final String GRANTED = "granted";
	public static final String DENIED = "denied";
	public static final String UNLOCKED = "unlocked";
	public static final String WAS_NOT_GRANTED = "was not granted";
	
	private final NodeService nodeService;
	private final ClientService clientService;
	private Optional<Node> authorized;
	private Boolean running;
	
	@Inject
	public CoordinatorService(NodeService nodeService, ClientService clientService) {
		this.nodeService = nodeService;
		this.clientService = clientService;
		running = Boolean.FALSE;
	}
	
	public void start() {
		authorized = Optional.empty();
		running = Boolean.TRUE;
	}
	
	public void stop() {
		running = Boolean.FALSE;
	}
	
	public Boolean isRunning() {
		return running;
	}
	
	public synchronized boolean handle(Payload payload) throws NodeNotFoundException {
		Node sender = nodeService.getSender(payload);
		switch (payload.getDataUtf8()) {
			case LOCK: lock(sender); return true;
			case UNLOCK: unlock(sender); return true;
			default: return false;
		}
	}
	
	private void lock(Node node) {
		if (authorized.isPresent()) {
			deny(node);
		} else {
			grant(node);
		}
	}
	
	private void unlock(Node node) {
		if (isAuthorized(node)) {
			unauthorize();
			clientService.send(UNLOCKED, node);
		} else {
			clientService.send(WAS_NOT_GRANTED, node);
		}
	}
	
	private void grant(Node node) {
		authorize(node);
		clientService.send(GRANTED, node);
	}
	
	private void deny(Node node) {
		clientService.send(DENIED, node);
	}
	
	private void authorize(Node node) {
		authorized = Optional.of(node);
	}
	
	private void unauthorize() {
		authorized = Optional.empty();
	}
	
	private Boolean isAuthorized(Node node) {
		return authorized.isPresent()
				? node.equals(authorized.get())
				: Boolean.FALSE;
	}
	
}
