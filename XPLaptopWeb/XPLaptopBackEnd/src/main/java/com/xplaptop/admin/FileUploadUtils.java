package com.xplaptop.admin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
				try {
					Files.delete(file);
				} catch (IOException e) {
					System.out.println("Could not delete the file!");
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

	public static void deleteIfNotExist(List<String> listFileName, String uploadPath) {
		Map<String, File> filesInFolder = new HashMap<>();
		
		Path path = Paths.get(uploadPath);
		File file = path.toFile();
		File[] listFilesName = file.listFiles();
		for(File f : listFilesName) {
			if(f.isFile()) {
				filesInFolder.put(f.getName(), f);
			}
		}
		for(String name : filesInFolder.keySet()) {
			if(!listFileName.contains(name)) {
				filesInFolder.get(name).delete();
			}
		}
	}
	
}
