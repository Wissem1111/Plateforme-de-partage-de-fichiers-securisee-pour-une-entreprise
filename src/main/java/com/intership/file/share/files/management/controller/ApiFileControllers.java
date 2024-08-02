package com.intership.file.share.files.management.controller;

import com.intership.file.share.files.management.message.ResponseFile;
import com.intership.file.share.files.management.model.dto.FileDto;
import com.intership.file.share.files.management.service.FileService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/files")
public class ApiFileControllers {
    @Autowired
    private FileService fileService;

    @PostMapping("/single/base")
    public ResponseEntity<ResponseFile> uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        FileDto fileDto = fileService.uploadFile(file);
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

        return ResponseEntity.ok(responseFile);
    }
    @PostMapping("/multiple/base")
    public ResponseEntity<List<ResponseFile>> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) throws Exception {
        List<ResponseFile> responseList = new ArrayList<>();
        for (MultipartFile file : files) {
            FileDto fileDto = fileService.uploadFile(file);
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
    }

    @GetMapping("/all")
    public ResponseEntity<List<ResponseFile>> getAllFiles() {
        List<FileDto> files = fileService.getAllFiles();

        List<ResponseFile> responseFiles = files.stream().map(file -> {
            String base64EncodedData = new String(file.getData());
            byte[] decodedBytes = Base64.decodeBase64(base64EncodedData);
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
    public ResponseEntity<ResponseFile> handleFileUpload(@RequestParam("file") MultipartFile file) {
        String fileName = file.getOriginalFilename();
        try {
            file.transferTo(new java.io.File("F:\\Work\\" + fileName));
            String downloadUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/download/")
                    .path(fileName)
                    .toUriString();
            ResponseFile response = new ResponseFile(
                    fileName,
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
    public ResponseEntity<List<ResponseFile>> handleMultipleFilesUpload(@RequestParam("files") MultipartFile[] files) {
        List<ResponseFile> responseList = new ArrayList<>();
        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            try {
                file.transferTo(new java.io.File("F:\\Work\\" + fileName));
                String downloadUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/download/")
                        .path(fileName)
                        .toUriString();
                ResponseFile response = new ResponseFile(
                        fileName,
                        downloadUrl,
                        file.getContentType(),
                        file.getSize()
                );
                responseList.add(response);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
        return ResponseEntity.ok(responseList);
    }
}