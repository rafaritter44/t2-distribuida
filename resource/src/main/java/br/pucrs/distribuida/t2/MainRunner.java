package br.pucrs.distribuida.t2;

import com.google.inject.Guice;
import com.google.inject.Injector;

import br.pucrs.distribuida.t2.service.ServerService;

public class MainRunner {
	
	public static void main(String[] args) throws InterruptedException {
		Injector injector = Guice.createInjector();
		injector.getInstance(ServerService.class).start();
		Thread.currentThread().join();
	}
	
}
