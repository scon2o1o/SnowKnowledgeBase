package net.javaguides.springboot.web;

import net.javaguides.springboot.model.Audit;
import net.javaguides.springboot.model.User;
import net.javaguides.springboot.service.AuditService;
import net.javaguides.springboot.service.SettingsService;
import net.javaguides.springboot.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class UserController {

    private UserService userService;
    private AuditService auditService;
    private SettingsService settingsService;

    public UserController(UserService userService, AuditService auditService, SettingsService settingsService) {
        super();
        this.userService = userService;
        this.auditService = auditService;
        this.settingsService = settingsService;
    }

    @GetMapping("/users")
    public String listUsers(Model model, Model settingsModel) {
        model.addAttribute("users", userService.getAllUsers());
        settingsModel.addAttribute("settings", settingsService.getAllSettings());
        List settings = settingsService.getAllSettings();
        if (settings.isEmpty()) {
            settingsModel.addAttribute("response", "NoData");
        } else {
            settingsModel.addAttribute("response", "");
        }
        return "users";
    }

    @GetMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            Audit audit = new Audit("User: " + user.getEmail() + " deleted");
            auditService.saveAudit(audit);
            userService.deleteUserById(id);
            return "redirect:/users?success";
        } catch (Exception e) {
            return "redirect:/users?fail";
        }
    }
}
