package br.pucrs.distribuida.t2;

import com.google.inject.Guice;
import com.google.inject.Injector;

import br.pucrs.distribuida.t2.module.FileModule;
import br.pucrs.distribuida.t2.module.NetworkModule;
import br.pucrs.distribuida.t2.module.NodeModule;

public class MainRunner {
	
	private static final int FILE = 0;
	private static final int LINE = 1;
	
	public static void main(String[] args) {
		FileModule fileModule = new FileModule();
		String file = args[FILE];
		Integer line = Integer.parseInt(args[LINE]);
		NodeModule nodeModule = new NodeModule(fileModule.getFileService(), file, line);
		NetworkModule networkModule = new NetworkModule(nodeModule.getNodeService().getSelf().getPort());
		Injector injector = Guice.createInjector(fileModule, nodeModule, networkModule);
	}
	
}
