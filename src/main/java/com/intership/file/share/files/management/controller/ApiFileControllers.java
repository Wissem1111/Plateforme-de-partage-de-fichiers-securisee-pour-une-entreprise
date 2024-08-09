package com.intership.file.share.files.management.controller;

import com.intership.file.share.auditLogs.management.service.AuditLogService;
import com.intership.file.share.auth.Repository.UserRepository;
import com.intership.file.share.auth.user.User;
import com.intership.file.share.files.management.message.ResponseFile;
import com.intership.file.share.files.management.model.dto.FileDto;
import com.intership.file.share.files.management.model.entity.File;
import com.intership.file.share.files.management.model.mapper.FileMapper;
import com.intership.file.share.files.management.repository.FileRepository;
import com.intership.file.share.files.management.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.core.io.Resource;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/files")
public class ApiFileControllers {
    private final FileService fileService;
    private final UserRepository userRepository;
    private final AuditLogService auditLogService;
    private final FileMapper fileMapper;
    private final FileRepository fileRepository;

    @Autowired
    public ApiFileControllers(FileService fileService, UserRepository userRepository,
                          AuditLogService auditLogService, FileMapper fileMapper,
                          FileRepository fileRepository) {
        this.fileService = fileService;
        this.userRepository = userRepository;
        this.auditLogService = auditLogService;
        this.fileMapper = fileMapper;
        this.fileRepository = fileRepository;
    }

    @Value("${file.upload-dir}")
    private String uploadDir;

    @GetMapping("/{id}")
    public ResponseEntity<FileDto> getFileById(@PathVariable Long id) throws Exception {
        FileDto fileDto = fileService.getFileById(id);
        return ResponseEntity.ok(fileDto);
    }


    @PostMapping("/single/base")
    public ResponseEntity<ResponseFile> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("userEmail") String userEmail) throws Exception {
        if (file.isEmpty() || userEmail == null) {
            return ResponseEntity.badRequest().build();
        }
        FileDto fileDto = fileService.uploadFile(file, userEmail);
        String downloadUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(fileDto.getFileName())
                .toUriString();

        ResponseFile responseFile = new ResponseFile(
                fileDto.getFileName(),
                downloadUrl,
                file.getContentType(),
                file.getSize()
        );
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email"));
        if (user != null) {
            auditLogService.logAction("UPLOAD", user, fileMapper.toEntity(fileDto));
        }

        return ResponseEntity.ok(responseFile);
    }

    @PostMapping("/multiple/base")
    public ResponseEntity<List<ResponseFile>> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files, @RequestParam("userEmail") String userEmail) {
        List<ResponseFile> responseList = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                FileDto fileDto = fileService.uploadFile(file, userEmail);
                String downloadUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/download/")
                        .path(fileDto.getFileName())
                        .toUriString();
                ResponseFile response = new ResponseFile(
                        fileDto.getFileName(),
                        downloadUrl,
                        file.getContentType(),
                        file.getSize()
                );
                responseList.add(response);

            } catch (Exception e) {
                errors.add("Failed to upload file: " + file.getOriginalFilename() + " - " + e.getMessage());
            }
        }
        if (errors.isEmpty()) {
            return ResponseEntity.ok(responseList);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(responseList);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<ResponseFile>> getAllFiles() {
        List<FileDto> files = fileService.getAllFiles();

        List<ResponseFile> responseFiles = files.stream().map(file -> {
            String base64EncodedData = new String(file.getData());
            byte[] decodedBytes = Base64.getDecoder().decode(base64EncodedData);
            String downloadURL = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/download/")
                    .path(file.getFileName())
                    .toUriString();

            return new ResponseFile(
                    file.getFileName(),
                    downloadURL,
                    file.getFileType(),
                    decodedBytes.length
            );
        }).collect(Collectors.toList());
        return ResponseEntity.ok(responseFiles);
    }

    @PostMapping("/single/file")
    public ResponseEntity<ResponseFile> handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("userEmail") String userEmail) {
        String fileName = file.getOriginalFilename();
        try {
            file.transferTo(new java.io.File("F:\\Work\\" + fileName));

            FileDto fileDto = fileService.uploadFile(file, userEmail);
            String downloadUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/download/")
                    .path(fileDto.getFileName())
                    .toUriString();

            ResponseFile response = new ResponseFile(
                    fileDto.getFileName(),
                    downloadUrl,
                    file.getContentType(),
                    file.getSize()
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/multiple/file")
    public ResponseEntity<List<ResponseFile>> handleMultipleFilesUpload(@RequestParam("files") MultipartFile[] files,  @RequestParam("userEmail") String userEmail) {
        try {
            List<ResponseFile> responseList = new ArrayList<>();
            for (MultipartFile file : files) {
                FileDto fileDto = fileService.uploadFile(file, userEmail);
                String downloadUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/download/")
                        .path(fileDto.getFileName())
                        .toUriString();
                ResponseFile response = new ResponseFile(
                        fileDto.getFileName(),
                        downloadUrl,
                        file.getContentType(),
                        file.getSize()
                );
                responseList.add(response);
            }
            return ResponseEntity.ok(responseList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{fileName}")
    public ResponseEntity<Void> deleteFile(@PathVariable String fileName) {
        try {
            File fileEntity = fileRepository.findByFileName(fileName)
                    .orElseThrow(() -> new Exception("File not found with name " + fileName));

            Path filePath = Paths.get(uploadDir, fileEntity.getFileName());
            Files.deleteIfExists(filePath);

            fileRepository.delete(fileEntity);

            if (fileEntity.getOwner() != null) {
                auditLogService.logAction("DELETE", fileEntity.getOwner(), fileEntity);
            }

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        try {
            System.out.println("Received request to download file: " + fileName);

            Resource resource = fileService.loadFileAsResource(fileName);
            if (resource == null || !resource.exists()) {
                System.out.println("File not found: " + fileName);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            System.out.println("Resource found at path: " + resource.getFile().getAbsolutePath());

            String contentType = "application/octet-stream";
            try {
                contentType = Files.probeContentType(Paths.get(resource.getFile().getAbsolutePath()));
            } catch (IOException e) {
                System.out.println("Could not determine file type.");
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
