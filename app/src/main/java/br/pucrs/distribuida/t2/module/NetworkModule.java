package br.pucrs.distribuida.t2.module;

import com.google.inject.AbstractModule;

import br.pucrs.distribuida.t2.service.NetworkService;

public class NetworkModule extends AbstractModule {
	
	private final int port;
	
	public NetworkModule(int port) {
		this.port = port;
	}
	
	@Override
	protected void configure() {
		bind(NetworkService.class).toInstance(new NetworkService(port));
	}

}
