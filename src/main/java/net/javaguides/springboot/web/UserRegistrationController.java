package net.javaguides.springboot.web;

import net.javaguides.springboot.model.Audit;
import net.javaguides.springboot.service.AuditService;
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

    public UserRegistrationController(UserService userService, AuditService auditService) {
        super();
        this.userService = userService;
        this.auditService = auditService;
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
            return "redirect:/users?success";
        } catch (Exception e) {
            return "redirect:/registration?fail";
        }
    }
}
