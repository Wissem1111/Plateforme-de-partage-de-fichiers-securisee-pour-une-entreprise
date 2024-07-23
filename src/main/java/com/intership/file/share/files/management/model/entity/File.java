package com.intership.file.share.files.management.model.entity;

import com.intership.file.share.users.management.model.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "\"File\"")


@Entity
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    private String type;
    @Column(nullable = false)
    private byte[] data;
    @Column(nullable = false, unique = true)
    private User owner;
    @Column(nullable = false)
    private List<String> permission;

}
