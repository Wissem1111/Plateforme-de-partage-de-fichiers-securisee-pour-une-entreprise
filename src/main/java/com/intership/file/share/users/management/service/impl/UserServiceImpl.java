package com.intership.file.share.users.management.service.impl;

import com.intership.file.share.users.management.model.Mapper.UserMapper;
import com.intership.file.share.users.management.model.dto.UserDto;
import com.intership.file.share.users.management.model.entity.User;
import com.intership.file.share.users.management.repository.UserRepository;
import com.intership.file.share.users.management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean verifyUser(String username, String password) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        return userOptional.isPresent() && userOptional.get().getPassword().equals(password);
    }

    @Override
    public UserDto registerUser(UserDto userDto) {
        User user = this.userMapper.toEntity(userDto);
        User savedUser = userRepository.save(user);
        return userMapper.toDTO(savedUser);
    }

    @Override
    public UserDto updateUser(Long id, UserDto updatedUserDto) {
        Optional<User> userOpt = userRepository.findById(id);
        User existingUser = userOpt.orElseThrow(() -> new RuntimeException("User not found"));
        existingUser.setUsername(updatedUserDto.getUsername());
        User updatedUser = userRepository.save(existingUser);
        return userMapper.toDTO(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDto getUserById(Long id) {
        Optional<User> userOpt = this.userRepository.findById(id);
        User user = userOpt.orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toDTO(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(userMapper::toDTO).collect(Collectors.toList());
    }
}
