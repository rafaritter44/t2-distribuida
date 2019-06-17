package br.pucrs.distribuida.t2.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.google.inject.Singleton;

@Singleton
public class FileService {
	
	private static final String FILE = "log.txt";
	
	public synchronized void writeToFile(String content) {
		try {
			Files.write(Paths.get(FILE), content.concat("\n").getBytes(),
					StandardOpenOption.CREATE, StandardOpenOption.APPEND);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
