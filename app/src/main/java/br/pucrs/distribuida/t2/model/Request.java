package br.pucrs.distribuida.t2.model;

public class Request {
	
	private final String message;
	private final String host;
	private final Integer port;
	
	public Request(String message, String host, Integer port) {
		this.message = message;
		this.host = host;
		this.port = port;
	}
	
	public String getMessage() {
		return message;
	}
	
	public String getHost() {
		return host;
	}
	
	public Integer getPort() {
		return port;
	}
	
}
