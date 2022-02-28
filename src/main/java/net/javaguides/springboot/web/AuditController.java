package net.javaguides.springboot.web;

import net.javaguides.springboot.model.Audit;
import net.javaguides.springboot.service.AuditService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuditController {
    private AuditService auditService;

    public AuditController(AuditService auditService) {
        super();
        this.auditService = auditService;
    }

    @GetMapping("/audit")
    public String listAudit(Model model){
        model.addAttribute("audit", auditService.getAllAudit());
        return "audit";
    }
    @PostMapping("/audit")
    public String saveAudit(@ModelAttribute("audit") Audit audit) {
        auditService.saveAudit(audit);
        return "Saved";
    }
}
