package com.intership.file.share.roles.management.model.mapper;

import com.intership.file.share.roles.management.model.dto.RoleDto;
import com.intership.file.share.roles.management.model.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel ="spring")
public interface RoleMapper {
    Role toEntity(RoleDto dto);
    RoleDto toDTO(Role role);
}
