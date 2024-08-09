package com.intership.file.share.auditLogs.management.service;

import com.intership.file.share.auth.user.User;
import com.intership.file.share.files.management.model.entity.File;

public interface AuditLogService {
    void logAction(String action, User user, File file);

}

