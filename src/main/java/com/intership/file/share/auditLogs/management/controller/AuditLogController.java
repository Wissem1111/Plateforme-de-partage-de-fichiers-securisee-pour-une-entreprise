package com.intership.file.share.auditLogs.management.controller;

import com.intership.file.share.auditLogs.management.entity.AuditLog;
import com.intership.file.share.auditLogs.management.entity.AuditLogDTO;
import com.intership.file.share.auditLogs.management.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/audit-logs")
public class AuditLogController {

    private final AuditLogRepository auditLogRepository;

    @Autowired
    public AuditLogController(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }


    @GetMapping
    public List<AuditLogDTO> getAllAuditLogs() {
        return auditLogRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuditLogDTO> getAuditLogById(@PathVariable Long id) {
        AuditLog auditLog = auditLogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AuditLog not found with id " + id));
        return ResponseEntity.ok(convertToDTO(auditLog));
    }

    private AuditLogDTO convertToDTO(AuditLog auditLog) {
        return new AuditLogDTO(
                auditLog.getId(),
                auditLog.getAction(),
                auditLog.getTimestamp(),
                auditLog.getUser().getEmail(),
                auditLog.getFile().getFileName()
        );
    }
}

