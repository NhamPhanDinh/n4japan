package com.haui.japanese.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import android.util.Log;

public class FileUntils {

	public static boolean deleteFile(File file) {
		return file.delete();
	}

	public static void ExtractFile(File file, File folder, String password) {

		try {
			ZipFile zipFile = new ZipFile(file.getPath());
			if (zipFile.isEncrypted()) {
				zipFile.setPassword(password);
			}
			zipFile.extractAll(folder.getPath());
		} catch (ZipException e) {
			e.printStackTrace();
		}

	}

	public static String readFileText(File file) {
		StringBuilder st = new StringBuilder();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;

			while ((line = br.readLine()) != null) {
				st.append(line);
				st.append('\n');
			}
			br.close();
		} catch (IOException e) {
			Log.e("file", e.toString());
		}
		return st.toString();

	}

}
