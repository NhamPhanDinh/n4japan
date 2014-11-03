package com.haui.japanese.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.google.android.gms.internal.fi;
import com.google.android.gms.internal.fo;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import android.util.Log;

public class FileUntils {

	/**
	 * Xóa 1 file khỏi thẻ nhớ
	 * 
	 * @param file
	 * @return
	 */
	public static boolean deleteFile(File file) {
		return file.delete();
	}

	/**
	 * Giải nén 1 file có kèm pass
	 * 
	 * @param file
	 * @param folder
	 * @param password
	 */
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

	public static void deleteFolder(File folder) {

		if (folder.isDirectory()) {
			File[] files = folder.listFiles();
			for (File f : files) {
				f.delete();
			}

		}

	}

	/**
	 * ĐỌc file dạng text
	 * 
	 * @param file
	 * @return
	 */
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
