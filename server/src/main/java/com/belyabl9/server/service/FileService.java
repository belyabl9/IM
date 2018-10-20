package com.belyabl9.server.service;

import java.io.FileOutputStream;
import java.io.IOException;

public class FileService {
	
	public static void saveMediaFile(byte[] blob, String filename) {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(filename);
			fos.write(blob);
			fos.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
