package com.intership.file.share.users.management.model.dto;

import com.intership.file.share.roles.management.enume.Role;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private String email;
    private Role roles;
}
