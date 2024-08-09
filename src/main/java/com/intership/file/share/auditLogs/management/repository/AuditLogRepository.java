package com.intership.file.share.auditLogs.management.repository;


import com.intership.file.share.auditLogs.management.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}

