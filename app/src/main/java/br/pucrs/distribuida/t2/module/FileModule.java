package br.pucrs.distribuida.t2.module;

import com.google.inject.AbstractModule;

import br.pucrs.distribuida.t2.service.FileService;

public class FileModule extends AbstractModule {
	
	private final FileService fileService;
	
	public FileModule() {
		this.fileService = new FileService();
	}
	
	@Override
	protected void configure() {
		bind(FileService.class).toInstance(fileService);
	}
	
	public FileService getFileService() {
		return fileService;
	}

}
