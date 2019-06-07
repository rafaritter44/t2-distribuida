package br.pucrs.distribuida.t2.node;

public class Node {
	
	private final Integer id;
	private final String host;
	private final Integer port;
	
	public Node(Integer id, String host, Integer port) {
		this.id = id;
		this.host = host;
		this.port = port;
	}
	
	public Integer getId() {
		return id;
	}
	
	public String getHost() {
		return host;
	}
	
	public Integer getPort() {
		return port;
	}
	
}
