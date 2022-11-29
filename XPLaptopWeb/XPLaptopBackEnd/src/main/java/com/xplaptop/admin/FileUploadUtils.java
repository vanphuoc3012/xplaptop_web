package com.xplaptop.admin;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtils {
	
	public static void uploadFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
		Path uploadPath = Paths.get(uploadDir);
		
		if(!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}
		
		InputStream inputStream = multipartFile.getInputStream();
		
		Path path = uploadPath.resolve(fileName);
		Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
		
	}
	
	public static void cleanDir(String uploadDir) throws IOException {
		Path uploadPath = Paths.get(uploadDir);
		if(Files.exists(uploadPath)) {
			Files.list(uploadPath).forEach(file -> {
				if(!Files.isDirectory(file)) {
					try {
						Files.delete(file);
					} catch (IOException e) {
						System.out.println("Could not delete the file!");
					}
				}
			});
		}
		
	}
	
	public static void main(String[] args) {
		String a = "users-photos";
		Path b = Paths.get(a);
		
		String c = b.toFile().getAbsolutePath();
		
		System.out.println(a);
		System.out.println(b);
		System.out.println(c);
	}
	
}
