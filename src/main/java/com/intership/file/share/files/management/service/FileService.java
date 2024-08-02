package com.intership.file.share.files.management.service;

import com.intership.file.share.files.management.model.dto.FileDto;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
public interface FileService {
    FileDto uploadFile(MultipartFile file) throws Exception;
    void saveFiles(MultipartFile[] files) throws Exception;
    List<FileDto> getAllFiles();
}