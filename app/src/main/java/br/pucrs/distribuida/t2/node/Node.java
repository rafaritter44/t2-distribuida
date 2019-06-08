package br.pucrs.distribuida.t2.node;

public class Node {
	
	private final Integer id;
	private final String host;
	private final Integer port;
	private Boolean coordinator;
	
	public Node(Integer id, String host, Integer port) {
		this.id = id;
		this.host = host;
		this.port = port;
		setCoordinator(Boolean.FALSE);
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
	
	public Boolean isCoordinator() {
		return coordinator;
	}
	
	public void setCoordinator(Boolean coordinator) {
		this.coordinator = coordinator;
	}
	
}
