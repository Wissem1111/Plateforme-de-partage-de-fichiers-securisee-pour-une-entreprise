package com.intership.file.share.files.management.repository;

import com.intership.file.share.files.management.model.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
}

