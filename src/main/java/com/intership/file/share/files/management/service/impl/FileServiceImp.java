package com.intership.file.share.files.management.service.impl;

import com.intership.file.share.files.management.model.dto.FileDto;
import com.intership.file.share.files.management.model.entity.File;
import com.intership.file.share.files.management.model.mapper.FileMapper;
import com.intership.file.share.files.management.repository.FileRepository;
import com.intership.file.share.files.management.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
@Service
public class FileServiceImp implements FileService {
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private FileMapper fileMapper;
    @Override
    public FileDto saveAttachment(MultipartFile file) throws Exception {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
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
            fileEntity.setData(file.getBytes());
            File savedFile = fileRepository.save(fileEntity);
            return fileMapper.toDTO(savedFile);
        } catch (MaxUploadSizeExceededException e) {
            throw new MaxUploadSizeExceededException(file.getSize());
        } catch (Exception e) {
            throw new Exception("Could not save File: " + fileName);
        }
    }
    @Override
    public void saveFiles(MultipartFile[] files) {
        Arrays.asList(files).forEach(file -> {
            try {
                saveAttachment(file);
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
}
