package net.javaguides.springboot.service;

import net.javaguides.springboot.model.Audit;
import net.javaguides.springboot.repository.AuditRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditServiceImpl implements AuditService{

    private AuditRepository auditRepository;

    public AuditServiceImpl(AuditRepository auditRepository) {
        super();
        this.auditRepository = auditRepository;
    }

    @Override
    public List<Audit> getAllAudit(){
        return auditRepository.findAll();
    }

    @Override
    public Audit saveAudit(Audit audit){
        return auditRepository.save(audit);
    }
}
