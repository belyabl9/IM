package utils;

import java.io.FileOutputStream;
import java.io.IOException;

public class FileService {
	
	private static String PATH_PREFIX = "/home/IM/";
	
	public static void saveFile(byte[] blob, String filename) {
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
