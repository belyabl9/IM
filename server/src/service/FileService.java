package service;

import java.io.FileOutputStream;
import java.io.IOException;

public class FileService {
	
	private static String MEDIA_PATH_PREFIX = "/home/IM/";
	
	public static void saveMediaFile(byte[] blob, String filename) {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(filename);
			fos.write(blob);
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
