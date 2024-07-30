package com.intership.file.share.files.management.model.mapper;

import com.intership.file.share.files.management.model.dto.FileDto;
import com.intership.file.share.files.management.model.entity.File;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import java.util.List;

@Mapper(componentModel = "spring")

public interface FileMapper {
    FileMapper INSTANCE = Mappers.getMapper(FileMapper.class);
    File toEntity(FileDto dto);
    FileDto toDTO(File entity);
    List<FileDto> toDTOList(List<File> files);

}
