package net.javaguides.springboot.service;


import net.javaguides.springboot.model.Audit;

import java.util.List;

public interface AuditService {
    List<Audit> getAllAudit();

    Audit saveAudit(Audit audit);
}
