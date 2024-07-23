package com.intership.file.share.files.management.repository;

import com.intership.file.share.files.management.model.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Long> {
    Optional<File> findByName(String name);
}