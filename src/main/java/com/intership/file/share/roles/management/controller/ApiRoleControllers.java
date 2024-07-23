package com.intership.file.share.roles.management.controller;

import com.intership.file.share.roles.management.model.dto.RoleDto;
import com.intership.file.share.roles.management.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/Roles")
public class ApiRoleControllers {
    @Autowired
    private RoleService roleService;

    @GetMapping(value = "/{id}")
    public RoleDto getRole(@PathVariable("id") Long id){
        return roleService.getRoleById(id);
    }
    @GetMapping(value = "/all")
    public List<RoleDto> getRoles(){
        return roleService.getAllRoles();
    }
    @PostMapping(value = "/roles")
    public RoleDto addRole(@RequestBody RoleDto roleDto) {
        return roleService.addRoles(roleDto);
    }
    @PutMapping("/Update/{id}")
    public RoleDto updateRole(@PathVariable("id") Long id, @RequestBody  RoleDto roleDto){
        return roleService.updateRole(id,roleDto);
    }
    @DeleteMapping(value = "/Delete/{id}")
    public void deleteRole(@PathVariable Long id){
        roleService.deleteRole(id);
    }
}
