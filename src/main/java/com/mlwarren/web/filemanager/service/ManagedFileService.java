package com.mlwarren.web.filemanager.service;

import com.mlwarren.web.filemanager.exception.FileStorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@PropertySource("classpath:application.properties")
public class ManagedFileService {

    @Value("${file.upload-dir}")
    private String UPLOAD_DIR;

    public String saveFileToDisk(MultipartFile file) {
        try {
            Files.createDirectories(Paths.get(UPLOAD_DIR));
        } catch (Exception ex) {
            throw new FileStorageException("Could not create upload directory, check path and permissions");
        }

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            //If filename contains .. reject it
            if (fileName.contains("..")) {
                throw new FileStorageException("Invalid filename path sequence: " + fileName);
            }

            Path targetLocation = Paths.get(UPLOAD_DIR).resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;

        } catch (IOException e) {
            throw new FileStorageException("Unable to store file: " + fileName);
        }
    }

    public Resource loadFileAsResourceFromDisk(String fileName) {
        try {
            Path filePath = Paths.get(UPLOAD_DIR).resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new FileStorageException("File not found: " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new FileStorageException("File not found: " + fileName);
        }
    }

    public String deleteFileFromDisk(String fileName){
        try {
            Path filePath = Paths.get(UPLOAD_DIR).resolve(fileName).normalize();
            Files.delete(filePath);
            return filePath.toUri().toString();
        }
        catch(IOException ex){
            throw new FileStorageException("File not found: " + fileName);
        }
    }
}
