package com.intership.file.share.files.management.model.dto;

import com.intership.file.share.files.management.model.entity.File;
import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FileDto {
    private String fileName;
    private String fileType;
    private byte[] data;

    public FileDto(File entity) {
        this.fileName = entity.getFileName();
        this.fileType = entity.getFileType();
        this.data = entity.getData();

    }
}


