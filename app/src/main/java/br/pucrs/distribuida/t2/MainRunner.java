package br.pucrs.distribuida.t2;

import com.google.inject.Guice;
import com.google.inject.Injector;

import br.pucrs.distribuida.t2.module.FileModule;
import br.pucrs.distribuida.t2.module.NodeModule;
import br.pucrs.distribuida.t2.service.CoordinatorService;
import br.pucrs.distribuida.t2.service.RandomRequestService;
import br.pucrs.distribuida.t2.service.ServerService;

public class MainRunner {
	
	private static final int FILE = 0;
	private static final int LINE = 1;
	
	public static void main(String[] args) {
		FileModule fileModule = new FileModule();
		String file = args[FILE];
		Integer line = Integer.parseInt(args[LINE]);
		NodeModule nodeModule = new NodeModule(fileModule.getFileService(), file, line);
		Injector injector = Guice.createInjector(fileModule, nodeModule);
		if (nodeModule.getNodeService().isCoordinator()) {
			injector.getInstance(CoordinatorService.class).start();
		}
		injector.getInstance(ServerService.class).start();
		injector.getInstance(RandomRequestService.class).start();
	}
	
}
