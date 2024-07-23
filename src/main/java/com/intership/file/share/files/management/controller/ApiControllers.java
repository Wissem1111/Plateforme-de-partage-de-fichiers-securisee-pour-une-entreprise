package com.intership.file.share.files.management.controller;

import com.intership.file.share.files.management.model.dto.FileDto;
import com.intership.file.share.files.management.model.entity.File;
import com.intership.file.share.files.management.service.FileService;
import com.intership.file.share.roles.management.model.dto.RoleDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(name = "/Files")
public class ApiFileControllers {
    @Autowired
    private FileService fileService;

    @GetMapping(value = "/{id}")

    public FileDto getFile(@PathVariable("id") Long id) {

        return fileService.getFileById(id);
    }

    @GetMapping(value = "/all")
    public List<FileDto> getAllFiles() {
        return fileService.getAllFile();
    }

    @PostMapping(value = "/files")
        public FileDto uploadFile(@RequestBody FileDto fileDto) {
            return fileService.uploadFile(fileDto);
    }
    @GetMapping(value = "/files")
        public FileDto DownloadFile(String name){
            return fileService.downloadFile(name);
}
}
