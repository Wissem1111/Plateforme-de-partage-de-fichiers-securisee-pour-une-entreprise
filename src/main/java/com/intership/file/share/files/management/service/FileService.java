package com.intership.file.share.files.management.service;

import com.intership.file.share.files.management.model.dto.FileDto;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
public interface FileService {
    FileDto uploadFile(MultipartFile file,String userEmail)  throws Exception;
    public FileDto getFileById( Long id) throws Exception;
    void saveFiles(MultipartFile[] files,String userEmail) throws Exception;
    List<FileDto> getAllFiles();
    void deleteFile(String fileName) throws Exception;

    void storeFile(MultipartFile file);

    public Resource loadFileAsResource(String fileName);

}