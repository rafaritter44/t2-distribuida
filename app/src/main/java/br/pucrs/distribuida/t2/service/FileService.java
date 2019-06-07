package br.pucrs.distribuida.t2.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.google.inject.Singleton;

@Singleton
public class FileService {
	
	public List<String> readAllLines(String file) {
		try {
			return Files.readAllLines(Paths.get(file));
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
}
