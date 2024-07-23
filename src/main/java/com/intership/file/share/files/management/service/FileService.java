package com.intership.file.share.files.management.service;

import com.intership.file.share.files.management.model.AccessTypeEnum;
import com.intership.file.share.files.management.model.dto.FileDto;
import com.intership.file.share.users.management.model.entity.User;

import java.util.List;

public interface FileService {
    public FileDto uploadFile(FileDto fileDto);
    public FileDto downloadFile(String name);
    void shareFile(Long id, User user, AccessTypeEnum accessType);
    byte[] encryptFile(byte[] data);
    byte[] decryptFile(byte[] data);
    public FileDto getFileById(Long id);
    public List<FileDto> getAllFile() ;
}
