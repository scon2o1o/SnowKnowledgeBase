package net.javaguides.springboot.web;

import net.javaguides.springboot.model.Audit;
import net.javaguides.springboot.service.AuditService;
import net.javaguides.springboot.service.SmtpService;
import net.javaguides.springboot.service.UserService;
import net.javaguides.springboot.web.dto.UserRegistrationDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/registration")
public class UserRegistrationController {

    private UserService userService;
    private AuditService auditService;
    private SmtpService smtpService;

    public UserRegistrationController(UserService userService, AuditService auditService, SmtpService smtpService) {
        super();
        this.userService = userService;
        this.auditService = auditService;
        this.smtpService = smtpService;
    }

    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }

    @GetMapping
    public String showRegistrationForm() {
        return "registration";
    }

    @PostMapping
    public String registerUserAccount(@ModelAttribute("user") UserRegistrationDto registrationDto) {
        try {
            Audit audit = new Audit("User: " + registrationDto.getEmail() + " added");
            auditService.saveAudit(audit);
            userService.save(registrationDto);
            smtpService.sendEmail(registrationDto.getEmail(), "User account created", "Hi " + registrationDto.getFirstName() + " " + registrationDto.getLastName() + ",\n\nA new user account has been added for you on the knowledge base. Please log in.\n\nUsername: " + registrationDto.getEmail() + "\nPassword: Please contact the system administrator\n\nRegards,\nSystem Administrator");
            return "redirect:/users?success";
        } catch (Exception e) {
            return "redirect:/registration?fail";
        }
    }
}
