package com.intership.file.share.users.management.model.Mapper;


import com.intership.file.share.users.management.model.dto.UserDto;
import com.intership.file.share.users.management.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "id",target = "id")
    User toEntity(UserDto dto);
    UserDto toDTO(User entity);
}
