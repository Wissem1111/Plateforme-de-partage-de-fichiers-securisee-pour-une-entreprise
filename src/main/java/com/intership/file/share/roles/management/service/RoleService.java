package com.intership.file.share.roles.management.service;

import com.intership.file.share.roles.management.model.dto.RoleDto;
import java.util.List;

public interface RoleService {
        RoleDto addRoles(RoleDto roleDto);
        RoleDto updateRole(Long id, RoleDto updatedRoleDto);
        void deleteRole(Long id);
        RoleDto getRoleById(Long id);
        List<RoleDto> getAllRoles();
}








