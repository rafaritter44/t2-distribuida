package br.pucrs.distribuida.t2.module;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.google.inject.AbstractModule;

import br.pucrs.distribuida.t2.node.Node;
import br.pucrs.distribuida.t2.service.FileService;
import br.pucrs.distribuida.t2.service.NodeService;

public class NodeModule extends AbstractModule {
	
	private static final String DELIMITER = " ";
	private static final int ID = 0;
	private static final int HOST = 1;
	private static final int PORT = 2;
	
	private final NodeService nodeService;
	private final FileService fileService;
	private final String file;
	private final Integer line;
	
	public NodeModule(FileService fileService, String file, Integer line) {
		this.fileService = fileService;
		this.file = file;
		this.line = line;
		this.nodeService = nodeService();
	}
	
	@Override
	protected void configure() {
		bind(NodeService.class).toInstance(nodeService);
	}
	
	public NodeService getNodeService() {
		return nodeService;
	}
	
	private NodeService nodeService() {
		List<Node> nodes = toNodes(fileService.readAllLines(file));
		setCoordinator(nodes);
		Node self = self(nodes);
		return new NodeService(self, nodes);
	}
	
	private List<Node> toNodes(List<String> lines) {
		return lines.stream()
				.map(this::toNode)
				.collect(Collectors.toList());
	}
	
	private Node toNode(String line) {
		String[] splittedLine = line.split(DELIMITER);
		Integer id = Integer.parseInt(splittedLine[ID]);
		String host = splittedLine[HOST];
		Integer port = Integer.parseInt(splittedLine[PORT]);
		return new Node(id, host, port);
	}
	
	private void setCoordinator(List<Node> nodes) {
		nodes.stream()
				.max(Comparator.comparing(Node::getId))
				.ifPresent(node -> node.setCoordinator(Boolean.TRUE));
	}
	
	private Node self(List<Node> nodes) {
		return nodes.get(line);
	}

}
