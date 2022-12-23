package com.shopme.admin;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.junit.jupiter.api.DisplayNameGenerator.Standard;
import org.springframework.web.multipart.MultipartFile;

public class FileUploadUntil {

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
						System.out.println("Could not delete file : " + file);
					}
				}
			});
		} catch (IOException ex) {
			System.out.println("Could not list directory : " + dirPath);
		}
	}
}
