package br.pucrs.distribuida.t2.exception;

public class NodeNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -6217600712230408517L;
	
	public NodeNotFoundException(String message, Object... args) {
		super(String.format(message, args));
	}

}
