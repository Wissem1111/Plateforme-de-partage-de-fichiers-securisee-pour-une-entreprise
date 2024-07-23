package com.intership.file.share.users.management.service;
import com.intership.file.share.users.management.model.dto.UserDto;
import com.intership.file.share.users.management.model.entity.User;

import java.util.List;
public interface UserService {
    public boolean verifyUser(String username,String password );
    public UserDto registerUser(UserDto userDto);
    public UserDto updateUser(Long id ,UserDto updatedUserDto);
    public void deleteUser(Long id);
    public UserDto getUserById(Long id);
    public List<UserDto> getAllUsers();
}

