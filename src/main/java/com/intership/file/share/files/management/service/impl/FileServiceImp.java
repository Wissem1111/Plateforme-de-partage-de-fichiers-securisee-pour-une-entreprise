package com.intership.file.share.files.management.service.impl;

import com.intership.file.share.auditLogs.management.service.AuditLogService;
import com.intership.file.share.auth.Repository.UserRepository;
import com.intership.file.share.files.management.model.dto.FileDto;
import com.intership.file.share.files.management.model.entity.File;
import com.intership.file.share.files.management.model.mapper.FileMapper;
import com.intership.file.share.files.management.repository.FileRepository;
import com.intership.file.share.files.management.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import com.intership.file.share.auth.user.User;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class FileServiceImp implements FileService {


    private final FileRepository fileRepository;
    private final FileMapper fileMapper;
    private final UserRepository userRepository;
    private final AuditLogService auditLogService;
    private final Path fileStorageLocation;

    public FileServiceImp(FileRepository fileRepository, FileMapper fileMapper, UserRepository userRepository,
                          AuditLogService auditLogService, @Value("${file.upload-dir}") String uploadDir) {
        this.fileRepository = fileRepository;
        this.fileMapper = fileMapper;
        this.userRepository = userRepository;
        this.auditLogService = auditLogService;
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }
    @Override
    public FileDto uploadFile(MultipartFile file, String userEmail) throws Exception {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.trim().isEmpty()) {
            throw new Exception("Invalid file: the filename is missing.");
        }
        String fileName = StringUtils.cleanPath(originalFilename);
        Optional<File> existingFile = fileRepository.findByFileName(fileName);
        if (existingFile.isPresent()) {
            throw new Exception("File with name " + fileName + " already exists and cannot be uploaded again.");
        }

        try {
            if (fileName.contains("..")) {
                throw new Exception("Filename contains invalid path sequence " + fileName);
            }
            if (file.getBytes().length > (1024 * 1024)) {
                throw new Exception("File size exceeds maximum limit");
            }

            File fileEntity = new File();
            fileEntity.setFileName(fileName);
            fileEntity.setFileType(file.getContentType());


            String base64EncodedData = Base64.getEncoder().encodeToString(file.getBytes());
            fileEntity.setData(base64EncodedData.getBytes());
            User user = null;

            if (userEmail != null && !userEmail.isEmpty()) {
                Optional<User> optionalUser = userRepository.findByEmail(userEmail);
                if (optionalUser.isPresent()) {
                    user = optionalUser.get();
                    fileEntity.setOwner(user);
                } else {
                    throw new Exception("User not found with email " + userEmail);
                }
            }
            File savedFile = fileRepository.save(fileEntity);

            if (user != null) {
                auditLogService.logAction("UPlOAD", user, savedFile);
            }

            return fileMapper.toDTO(savedFile);
        } catch (MaxUploadSizeExceededException e) {
            throw new MaxUploadSizeExceededException(file.getSize());
        } catch (Exception e) {
            throw new Exception("Could not save File: " + fileName);
        }
    }

    @Override
    public FileDto getFileById( Long id) throws Exception {
        File file = fileRepository.findById(id)
                .orElseThrow(() -> new Exception("File not found with id " + id));
        return fileMapper.toDTO(file);
    }
    @Override
    public void saveFiles(MultipartFile[] files,String userEmail) {
        Arrays.asList(files).forEach(file -> {
            try {
                uploadFile(file,userEmail);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
    @Override
    public List<FileDto> getAllFiles() {
        List<File> files = fileRepository.findAll();
        return fileMapper.toDTOList(files);
    }

    @Override
    public void deleteFile(String fileName) throws Exception {
        File fileEntity = fileRepository.findByFileName(fileName)
                .orElseThrow(() -> new Exception("File not found with name " + fileName));

        Path filePath = fileStorageLocation.resolve(fileEntity.getFileName());
        Files.deleteIfExists(filePath);

        fileRepository.delete(fileEntity);

        if (fileEntity.getOwner() != null) {
            auditLogService.logAction("DELETE", fileEntity.getOwner(), fileEntity);
        }
    }


    @Override
    public void storeFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Failed to store empty file.");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.trim().isEmpty()) {
            throw new IllegalArgumentException("Failed to store file: filename is missing.");
        }
        try {
            Path targetLocation = this.fileStorageLocation.resolve(originalFilename);
            Files.createDirectories(targetLocation.getParent());
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new RuntimeException("Failed to store file " + originalFilename, ex);
        }
    }


    @Override
    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            System.out.println("Trying to load file from path: " + filePath.toAbsolutePath());
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                System.out.println("File not found or not readable: " + fileName);
                throw new FileNotFoundException("File not found or not readable " + fileName);
            }
        } catch (MalformedURLException ex) {
            System.out.println("Malformed URL for file: " + fileName);
            throw new RuntimeException("Malformed URL for file " + fileName, ex);
        } catch (IOException ex) {
            System.out.println("IO exception while accessing file: " + fileName);
            throw new RuntimeException("IO exception while accessing file " + fileName, ex);
        }
    }
}