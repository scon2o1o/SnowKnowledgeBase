package net.knowledgebase.springboot.service;


import net.knowledgebase.springboot.model.Audit;

import java.util.List;

public interface AuditService {
    List<Audit> getAllAudit();

    Audit saveAudit(Audit audit);
}
