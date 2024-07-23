package com.intership.file.share.files.management.model.dto;

import com.intership.file.share.users.management.model.entity.User;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FileDto {
    private int id;
    private String name;
    private String type;
    private byte[] data;
    private User owner;
    private List<String> permission;
}
