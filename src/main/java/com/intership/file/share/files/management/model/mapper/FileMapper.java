package com.intership.file.share.files.management.model.mapper;

import com.intership.file.share.files.management.model.dto.FileDto;
import com.intership.file.share.files.management.model.entity.File;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
@Mapper(componentModel = "spring")

public interface FileMapper {
    @Mapping(source = "id",target = "id")
    File toEntity(FileDto dto);
    FileDto toDTO(File entity);
}




