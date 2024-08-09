package com.intership.file.share.auditLogs.management.service.Impl;
import com.intership.file.share.auditLogs.management.entity.AuditLog;
import com.intership.file.share.auditLogs.management.repository.AuditLogRepository;
import com.intership.file.share.auditLogs.management.service.AuditLogService;

import com.intership.file.share.auth.user.User;
import com.intership.file.share.files.management.model.entity.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuditLogServiceImp implements AuditLogService {

    private final AuditLogRepository auditLogRepository;
    @Autowired
    public AuditLogServiceImp(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }
    @Override
    public void logAction(String action, User user, File file) {
        if (action == null || user == null || file == null) {
            throw new IllegalArgumentException("Action, User, and File cannot be null");
        }
        AuditLog auditLog = new AuditLog();
        auditLog.setAction(action);
        auditLog.setTimestamp(new Date());
        auditLog.setUser(user);
        auditLog.setFile(file);
        auditLogRepository.save(auditLog);
    }

}





