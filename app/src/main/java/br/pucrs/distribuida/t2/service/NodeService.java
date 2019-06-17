package br.pucrs.distribuida.t2.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import br.pucrs.distribuida.t2.exception.NodeNotFoundException;
import br.pucrs.distribuida.t2.model.Node;
import io.rsocket.Payload;

public class NodeService {
	
	private static final int IP = 0;
	private static final int PORT = 1;
	
	private final Node self;
	private final List<Node> nodes;
	private Boolean coordinatorOOS;
	
	public NodeService(Node self, List<Node> nodes) {
		this.self = self;
		this.nodes = nodes;
		coordinatorOOS = Boolean.FALSE;
	}
	
	public Node getSelf() {
		return self;
	}
	
	public List<Node> allNodesButMe() {
		return nodes.stream()
				.filter(node -> !node.sameAs(self))
				.collect(Collectors.toList());
	}
	
	public Boolean coordinatorIsOOS() {
		return coordinatorOOS;
	}
	
	public void coordinatorOOS() {
		coordinatorOOS = Boolean.TRUE;
	}
	
	public void setCoordinator(Node coordinator) {
		unsetCoordinator();
		nodes.stream()
				.filter(node -> node.sameAs(coordinator))
				.findAny()
				.orElseThrow(() -> new NodeNotFoundException("New coordinator not found: %s:%s",
						coordinator.getHost(), coordinator.getPort()))
				.setCoordinator(Boolean.TRUE);
		coordinatorOOS = Boolean.FALSE;
		System.out.println(String.format("New coordinator: node %d", coordinator.getId()));
	}
	
	private void unsetCoordinator() {
		getCoordinator().setCoordinator(Boolean.FALSE);
	}
	
	public Node getCoordinator() {
		return nodes.stream()
				.filter(Node::isCoordinator)
				.findAny()
				.orElseThrow(() -> new NodeNotFoundException("Coordinator node not found!"));
	}
	
	public Optional<Node> find(String host, Integer port) {
		return nodes.stream()
				.filter(node -> node.same(host, port))
				.findAny();
	}
	
	public boolean isCoordinator() {
		return getCoordinator().same(self.getHost(), self.getPort());
	}
	
	public List<Node> biggerThanMe() {
		return nodes.stream()
				.filter(this::isBiggerThanMe)
				.collect(Collectors.toList());
	}
	
	private boolean isBiggerThanMe(Node other) {
		return other.getId() > self.getId();
	}
	
	public Node getSender(Payload payload) {
		String[] ipAndPort = payload.getMetadataUtf8().split(":");
		String ip = ipAndPort[IP];
		int port = Integer.parseInt(ipAndPort[PORT]);
		return find(ip, port).orElseThrow(() -> new NodeNotFoundException("Node not found: %s:%d", ip, port));
	}

}
