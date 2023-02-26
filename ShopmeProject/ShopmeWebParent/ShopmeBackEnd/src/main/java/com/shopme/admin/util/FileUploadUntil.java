package com.shopme.admin.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class FileUploadUntil {
	private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadUntil.class);

	// save file
	public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
		Path uploadPath = Paths.get(uploadDir);
		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}
		try (InputStream inputStream = multipartFile.getInputStream()) {
			Path filPath = uploadPath.resolve(fileName);
			Files.copy(inputStream, filPath, StandardCopyOption.REPLACE_EXISTING);

		} catch (IOException e) {
			throw new IOException("Could not save file: " + fileName, e);
		}
	}

	// clean file
	public static void cleanDir(String dir) {
		Path dirPath = Paths.get(dir);

		try {
			Files.list(dirPath).forEach(file -> {
				if (!Files.isDirectory(file)) {
					try {
						Files.delete(file);
					} catch (IOException e) {
						LOGGER.error("Could not delete file : " + file);
						// System.out.println("Could not delete file : " + file);
					}
				}
			});
		} catch (IOException ex) {
			LOGGER.error("Could not list directory : " + dirPath);
		}
	}

	public static void removeDir(String dir) {
		cleanDir(dir);
		try {
			Files.delete(Paths.get(dir));
		} catch (IOException e) {
			LOGGER.error("Could not remove directory : " + dir);
		}
	}
}
