package com.mlwarren.web.filemanager.resource;

import com.mlwarren.web.filemanager.entity.ManagedFile;
import com.mlwarren.web.filemanager.entity.User;
import com.mlwarren.web.filemanager.exception.FileDetailsNotFoundException;
import com.mlwarren.web.filemanager.exception.UserNotFoundException;
import com.mlwarren.web.filemanager.repository.ManagedFileRepository;
import com.mlwarren.web.filemanager.repository.UserRepository;
import com.mlwarren.web.filemanager.service.ManagedFileService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Log
@RestController
public class ManagedFileResource {

    @Autowired
    ManagedFileRepository managedFileRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ManagedFileService managedFileService;

    @GetMapping("/users/{id}/files")
    public Resources<ManagedFile> retrieveAllFileDetailsForUser(@PathVariable Integer id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent())
            throw new UserNotFoundException("UserID not found: " + id);

        User user = optionalUser.get();
        List<ManagedFile> managedFilesList = user.getFiles();

        return new Resources<ManagedFile>(managedFilesList);
    }

    @GetMapping("/users/{id}/files/{fid}")
    public Resource<ManagedFile> retrieveFileDetailForFile(@PathVariable Integer id, @PathVariable Integer fid) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent())
            throw new UserNotFoundException("UserID not found: " + id);

        User user = optionalUser.get();
        List<ManagedFile> managedFilesList = user.getFiles();
        ManagedFile mf = null;
        for (ManagedFile file : managedFilesList) {
            if (file.getId().equals(fid)) {
                mf = file;
                break;
            }
        }

        if (mf == null)
            throw new FileDetailsNotFoundException("File with UserID: " + fid + " and FileID: " + fid + " not found.");

        Resource<ManagedFile> resource = new Resource<ManagedFile>(mf);
        ControllerLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllFileDetailsForUser(id));

        resource.add(linkTo.withRel("all-file-details-for-user"));
        return resource;
    }

    @GetMapping("/users/{id}/files/{fid}/download")
    public ResponseEntity<org.springframework.core.io.Resource> downloadFileByID(
            @PathVariable Integer id, @PathVariable Integer fid, HttpServletRequest request) {
        Optional<ManagedFile> optMf = managedFileRepository.findById(fid);
        if (!optMf.isPresent())
            throw new FileDetailsNotFoundException("Managed file not found");

        ManagedFile mf = optMf.get();
        org.springframework.core.io.Resource resource =
                managedFileService.loadFileAsResourceFromDisk(mf.getFileName());

        //Update download count and save update to db
        Integer downloadCount = mf.getDownloadCount();
        downloadCount++;
        mf.setDownloadCount(downloadCount);
        managedFileRepository.save(mf);

        //Attempt to determine file content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            log.info("Can't find content type, falling back to default");
        }
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""
                        + resource.getFilename() + "\"")
                .body(resource);

    }

    @PostMapping("/users/{id}/files")
    public ManagedFile uploadFile(@PathVariable Integer id, @RequestParam("file") MultipartFile file) {
        //Save file to disk
        String fileName = managedFileService.saveFileToDisk(file);

        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent())
            throw new UserNotFoundException("UserID not found: " + id);

        ManagedFile mf = new ManagedFile();
        mf.setFileName(fileName);
        mf.setFileSize(file.getSize());
        mf.setFileType(file.getContentType());
        mf.setDownloadCount(0);
        mf.setUser(user.get());
        managedFileRepository.save(mf);

        //Update database with managed file information
        return mf;
    }

    @DeleteMapping("/users/{id}/files/{fid}")
    public Resource<ManagedFile> deleteFile(@PathVariable Integer id, @PathVariable Integer fid){
        Optional<ManagedFile> optMf = managedFileRepository.findById(fid);
        if (!optMf.isPresent())
            throw new FileDetailsNotFoundException("Managed file not found");

        ManagedFile mf = optMf.get();
        String fileName = mf.getFileName();

        managedFileService.deleteFileFromDisk(fileName);
        managedFileRepository.delete(mf);

        Resource<ManagedFile> resource = new Resource<ManagedFile>(mf);
        ControllerLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllFileDetailsForUser(id));

        resource.add(linkTo.withRel("all-file-details-for-user"));

        return resource;

    }
}
