package net.javaguides.springboot.web;

import net.javaguides.springboot.model.Audit;
import net.javaguides.springboot.model.Smtp;
import net.javaguides.springboot.service.AuditService;
import net.javaguides.springboot.service.SmtpService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SmtpController {

    private SmtpService smtpService;
    private AuditService auditService;

    public SmtpController(SmtpService smtpService, AuditService auditService) {
        super();
        this.smtpService = smtpService;
        this.auditService = auditService;
    }

    @GetMapping("/smtp")
    public String listSmtp(Model model) {
        model.addAttribute("smtp", smtpService.getAllSmtp());
        return "smtp";
    }

    @GetMapping("/smtp/new")
    public String createSmtpForm(Model model) {

        Smtp smtp = new Smtp();
        model.addAttribute("smtp", smtp);
        return "create_smtp";
    }

    @PostMapping("/smtp")
    public String saveSmtp(@ModelAttribute("smtp") Smtp smtp) {
        try {
            Audit audit = new Audit("SMTP settings added");
            auditService.saveAudit(audit);
            smtpService.saveSmtp(smtp);
            return "redirect:/smtp?success";
        } catch (Exception e) {
            return "redirect:/smtp?fail";
        }
    }

    @GetMapping("/smtp/edit/{id}")
    public String editSmtpForm(@PathVariable Long id, Model model) {
        model.addAttribute("smtp", smtpService.getSmtpById(id));
        return "edit_smtp";
    }

    @PostMapping("/smtp/{id}")
    public String updateSmtp(@PathVariable Long id,
                             @ModelAttribute("smtp") Smtp smtp,
                             Model model) {
        Smtp existingSmtp = smtpService.getSmtpById(id);
        if (!existingSmtp.getServer().equals(smtp.getServer())) {
            Audit audit = new Audit("SMTP settings updated. Server updated from '" + existingSmtp.getServer() + "' to '" + smtp.getServer() + "'");
            auditService.saveAudit(audit);
        }
        if (existingSmtp.getPort()!=(smtp.getPort())) {
            Audit audit = new Audit("SMTP settings updated. Port updated from '" + existingSmtp.getPort() + "' to '" + smtp.getPort() + "'");
            auditService.saveAudit(audit);
        }
        if (!existingSmtp.getAuth().equals(smtp.getAuth())) {
            Audit audit = new Audit("SMTP settings updated. Port updated from '" + existingSmtp.getPort() + "' to '" + smtp.getPort() + "'");
            auditService.saveAudit(audit);
        }
        existingSmtp.setId(id);
        existingSmtp.setServer(smtp.getServer());
        existingSmtp.setPort(smtp.getPort());
        existingSmtp.setAuth(smtp.getAuth());
        existingSmtp.setStarttls(smtp.getStarttls());
        existingSmtp.setFromAddress(smtp.getFromAddress());
        existingSmtp.setUsername(smtp.getUsername());
        existingSmtp.setPassword(smtp.getPassword());
        smtpService.updateSmtp(existingSmtp);
        return "redirect:/smtp";
    }

    @GetMapping("/smtp/{id}")
    public String deleteSmtp(@PathVariable Long id) {
        try {
            Audit audit = new Audit("SMTP settings deleted");
            smtpService.deleteSmtpById(id);
            return "redirect:/smtp?success";
        } catch (Exception e) {
            return "redirect:/smtp?fail";
        }
    }
}
