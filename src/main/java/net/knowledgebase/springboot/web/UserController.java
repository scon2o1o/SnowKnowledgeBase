package net.knowledgebase.springboot.web;

import net.knowledgebase.springboot.model.Audit;
import net.knowledgebase.springboot.model.User;
import net.knowledgebase.springboot.repository.UserRepository;
import net.knowledgebase.springboot.service.AuditService;
import net.knowledgebase.springboot.service.SettingsService;
import net.knowledgebase.springboot.service.UserService;
import net.knowledgebase.springboot.web.dto.UserPasswordDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class UserController {

    private UserService userService;
    private AuditService auditService;
    private SettingsService settingsService;
    private UserRepository userRepository;

    public UserController(UserService userService, AuditService auditService, SettingsService settingsService, UserRepository userRepository) {
        super();
        this.userService = userService;
        this.auditService = auditService;
        this.settingsService = settingsService;
        this.userRepository = userRepository;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
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

    @GetMapping("users/password")
    public String editPasswordForm(Model model, Model settingsModel) {
        model.addAttribute("userPasswordDto", new UserPasswordDto());
        settingsModel.addAttribute("settings", settingsService.getAllSettings());
        List settings = settingsService.getAllSettings();
        if (settings.isEmpty()) {
            settingsModel.addAttribute("response", "NoData");
        } else {
            settingsModel.addAttribute("response", "");
        }
        return "edit_password";
    }

    @PostMapping("users/password")
    public String updatePassword(@ModelAttribute("userPasswordDto") UserPasswordDto userPasswordDto) {
        try {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentPrincipalName = authentication.getName();
            User existingUser = userRepository.findByEmail(currentPrincipalName);
            if (passwordEncoder.matches(userPasswordDto.getCurrentPassword(), existingUser.getPassword())) {
                existingUser.setPassword(userPasswordDto.getNewPassword());
                userService.updateUserPassword(existingUser);
                return "redirect:/users/password?success";
            } else {
                return "redirect:/users/password?fail";
            }
        } catch (Exception e) {
            return "redirect:/users/password?fail";
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
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
