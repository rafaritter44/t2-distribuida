package br.pucrs.distribuida.t2.service;

import java.util.Optional;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import br.pucrs.distribuida.t2.exception.NodeNotFoundException;
import br.pucrs.distribuida.t2.model.Node;
import br.pucrs.distribuida.t2.model.Request;

@Singleton
public class CoordinatorService {
	
	private final NodeService nodeService;
	private final NetworkService networkService;
	private final Thread runnable;
	private Optional<Node> authorized;
	
	@Inject
	public CoordinatorService(NodeService nodeService, NetworkService networkService) {
		this.nodeService = nodeService;
		this.networkService = networkService;
		runnable = new Thread(this::run);
		authorized = Optional.empty();
	}
	
	public void start() {
		runnable.start();
	}
	
	public void stop() {
		runnable.interrupt();
	}
	
	private void run() {
		while (true) {
			try {
				handle(networkService.receive());
			} catch(NodeNotFoundException e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
	private synchronized void handle(Request request) throws NodeNotFoundException {
		Node sender = getSender(request);
		switch (request.getMessage()) {
			case "lock": lock(sender); break;
			case "unlock": unlock(sender); break;
			default: networkService.send("unknown command", sender);
		}
	}
	
	private Node getSender(Request request) throws NodeNotFoundException {
		return nodeService.find(request.getHost(), request.getPort())
				.orElseThrow(() -> new NodeNotFoundException(
						"Unknown node: %s:%s", request.getHost(), request.getPort()));
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
			networkService.send("ok", node);
		} else {
			networkService.send("was not granted", node);
		}
	}
	
	private void grant(Node node) {
		authorize(node);
		networkService.send("granted", node);
	}
	
	private void deny(Node node) {
		networkService.send("denied", node);
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
