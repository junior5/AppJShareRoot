package br.univel.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

public class Tools {
	
	public String getExtension(String name) {

		String extension = "";

		int i = name.lastIndexOf('.');
		
		if (i > 0) {
			extension = name.substring(i+1);
		}

		return extension;
	}

	public String getNomeArquivo(String nome) {

		int pos = nome.lastIndexOf(".");
		if (pos > 0) {
			nome = nome.substring(0, pos);
		}

		return nome;
	}
	
	private static byte[] createChecksum(String filename) throws Exception {
		InputStream fis = new FileInputStream(filename);

		byte[] buffer = new byte[1024];
		MessageDigest complete = MessageDigest.getInstance("MD5");
		int numRead;

		do {
			numRead = fis.read(buffer);
			if (numRead > 0) {
				complete.update(buffer, 0, numRead);
			}
		} while (numRead != -1);

		fis.close();
		return complete.digest();
	}

	public String getMD5Checksum(String filename) {
		byte[] b;
		try {
			b = createChecksum(filename);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		String result = "";

		for (int i = 0; i < b.length; i++) {
			result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
		}
		return result;
	}

}
