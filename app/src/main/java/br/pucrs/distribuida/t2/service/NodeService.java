package br.pucrs.distribuida.t2.service;

import java.util.List;

import br.pucrs.distribuida.t2.exception.NodeNotFoundException;
import br.pucrs.distribuida.t2.node.Node;

public class NodeService {
	
	private final Node self;
	private final List<Node> nodes;
	
	public NodeService(Node self, List<Node> nodes) {
		this.self = self;
		this.nodes = nodes;
	}
	
	public Node getSelf() {
		return self;
	}
	
	public Node getCoordinator() {
		return nodes.stream()
				.filter(Node::isCoordinator)
				.findAny()
				.orElseThrow(() -> new NodeNotFoundException("Coordinator node not found!"));
	}

}
