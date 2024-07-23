package com.intership.file.share.files.management.service.impl;

import com.intership.file.share.files.management.model.AccessTypeEnum;
import com.intership.file.share.files.management.model.dto.FileDto;
import com.intership.file.share.files.management.model.entity.File;
import com.intership.file.share.files.management.model.mapper.FileMapper;
import com.intership.file.share.files.management.repository.FileRepository;
import com.intership.file.share.files.management.service.FileService;
import com.intership.file.share.users.management.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FileServiceImpl implements FileService {
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private FileMapper fileMapper;

    @Override
    public FileDto uploadFile(FileDto fileDto) {
        File file = this.fileMapper.toEntity(fileDto);
        File savedFile = fileRepository.save(file);
        return fileMapper.toDTO(savedFile);
    }

    @Override
    public FileDto downloadFile(String name) {
        Optional<File> fileOpt = this.fileRepository.findByName(name);
        File file = fileOpt.orElseThrow(() -> new RuntimeException("File not found"));
        return fileMapper.toDTO(file);
    }

    @Override
    public void shareFile(Long id, User user, AccessTypeEnum accessType) {
        Optional<File> fileOpt = fileRepository.findById((long) id);
        File file = fileOpt.orElseThrow(() -> new RuntimeException("File not found"));

        switch (accessType) {
            case ADMIN:
                    break;
                case USER:
                    break;
                case GUEST:
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported access type: " + accessType);
            }
    }

    @Override
    public byte[] encryptFile(byte[] data) {
        return new byte[0];
    }

    @Override
    public byte[] decryptFile(byte[] data) {
        return new byte[0];
    }

    @Override
    public FileDto getFileById(Long id) {
        Optional<File> fileOpt = this.fileRepository.findById(id);
        File file = fileOpt.orElseThrow(() -> new RuntimeException("File not found"));
        return fileMapper.toDTO(file);
    }

    @Override
    public List<FileDto> getAllFile() {
        List<File> files = fileRepository.findAll();
        return files.stream().map(fileMapper::toDTO).collect(Collectors.toList());
    }
}
