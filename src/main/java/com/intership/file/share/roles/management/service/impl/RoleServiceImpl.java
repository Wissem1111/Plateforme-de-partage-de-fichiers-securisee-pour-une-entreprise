package com.intership.file.share.roles.management.service.impl;

import com.intership.file.share.roles.management.model.dto.RoleDto;
import com.intership.file.share.roles.management.model.entity.Role;
import com.intership.file.share.roles.management.model.mapper.RoleMapper;
import com.intership.file.share.roles.management.repository.RoleRepository;
import com.intership.file.share.roles.management.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service

public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RoleMapper roleMapper;
    @Override
    public RoleDto addRoles(RoleDto roleDto) {
        Role role = this.roleMapper.toEntity(roleDto);
        Role savedRole = roleRepository.save(role);
        return roleMapper.toDTO(savedRole);
    }
    @Override
    public RoleDto updateRole(Long id, RoleDto updatedRoleDto) {
        Optional<Role> roleOpt = roleRepository.findById(id);
        Role existingRole = roleOpt.orElseThrow(() -> new RuntimeException("Role not found"));
        existingRole.setName(updatedRoleDto.getName());

        Role updatedRole = roleRepository.save(existingRole);
        return roleMapper.toDTO(updatedRole);
    }
    @Override
    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }
    @Override
    public RoleDto getRoleById(Long id) {
        Optional<Role> roleOpt = this.roleRepository.findById(id);
        Role role = roleOpt.orElseThrow(()-> new RuntimeException("Role not Found"));
        return  roleMapper.toDTO(role);
    }
    @Override
    public List<RoleDto> getAllRoles() {
        List<Role> roles= roleRepository.findAll();
        return roles.stream().map(roleMapper::toDTO).collect(Collectors.toList());
    }
}
