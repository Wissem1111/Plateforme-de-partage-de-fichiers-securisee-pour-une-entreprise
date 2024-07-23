package com.intership.file.share.users.management.controller;

import com.intership.file.share.users.management.model.dto.UserDto;
import com.intership.file.share.users.management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Users")
public class ApiUserControllers {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/")
    public String FirstPage() {
        return "Welcome";
    }

    @GetMapping(value="/{id}")
    public UserDto getUser(@PathVariable("id") Long id) {
        return userService.getUserById(id);
    }

    @GetMapping(value="/all")
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping(value = "/users")
    public UserDto addUser(@RequestBody UserDto userDto) {
        return userService.registerUser(userDto);
    }

    @PutMapping(value = "/Update/{id}")
    public UserDto updateUser(@PathVariable("id") Long id, @RequestBody UserDto userDto) {
        return userService.updateUser(id, userDto);
    }

    @DeleteMapping(value = "/Delete/{id}")
    public void deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
    }
}


