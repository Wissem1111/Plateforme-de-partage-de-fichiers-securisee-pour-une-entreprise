package com.intership.file.share.files.management.model.entity;

import com.intership.file.share.auditLogs.management.entity.AuditLog;
import com.intership.file.share.auth.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "files")
public class File  {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String fileName;
    private String fileType;

    @Lob
    private byte[] data;
    @ManyToOne()
    @JoinColumn(name = "owner_id")
    private User owner;
    @OneToMany(mappedBy = "file", cascade = CascadeType.ALL)
    private List<AuditLog> auditLogs;
}