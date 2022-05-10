package net.knowledgebase.springboot.web;

import net.bytebuddy.utility.RandomString;
import net.knowledgebase.springboot.exception.UserNotFoundException;
import net.knowledgebase.springboot.model.Settings;
import net.knowledgebase.springboot.model.User;
import net.knowledgebase.springboot.repository.SettingsRepository;
import net.knowledgebase.springboot.service.SmtpService;
import net.knowledgebase.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ForgotPasswordController {

    @Autowired
    private UserService userService;

    @Autowired
    private SmtpService smtpService;

    @Autowired
    private SettingsRepository settingsRepository;

    public Settings getSettings() {
        List<Settings> databaseSettings = settingsRepository.findAll();
        Settings settings = new Settings();
        settings.setId(databaseSettings.get(0).getId());
        settings.setUrl(databaseSettings.get(0).getUrl());
        settings.setEmail(databaseSettings.get(0).isEmail());
        return settings;
    }

    @GetMapping("/forgot_password")
    public String showForgotPasswordForm() {
        return "forgot_password_form";
    }

    @PostMapping("/forgot_password")
    public String processForgotPassword(HttpServletRequest request, Model model) {
        String email = request.getParameter("email");
        String token = RandomString.make(30);
        try {
            userService.updateResetPasswordToken(token, email);
            Settings settings = getSettings();
            String resetPasswordLink = settings.getUrl() + "/reset_password?token=" + token;
            String content = "Hello,"
                    + "\n\nYou have requested to reset your password."
                    + " Click the link below to change your password: \n\n"
                    + resetPasswordLink
                    + "\n\nIgnore this email if you remember your password, "
                    + "or if you have not made the request.";
            smtpService.sendEmail(email, "Knowledge Base Password Reset", content);
            model.addAttribute("message", "We have sent a reset password link to your email. Please check.");
        } catch (UserNotFoundException ex) {
            model.addAttribute("error", ex.getMessage());
        }
        return "forgot_password_form";
    }

    @GetMapping("/reset_password")
    public String showResetPasswordForm(@Param(value = "token") String token, Model model) {
        User user = userService.getByResetPasswordToken(token);
        model.addAttribute("token", token);
        if (user == null) {
            model.addAttribute("message", "Invalid Token");
            return "message";
        }
        return "reset_password_form";
    }

    @PostMapping("/reset_password")
    public String processResetPassword(HttpServletRequest request, Model model) {
        String token = request.getParameter("token");
        String password = request.getParameter("password");
        User user = userService.getByResetPasswordToken(token);
        model.addAttribute("title", "Reset your password");
        if (user == null) {
            model.addAttribute("message", "Invalid Token");
            return "message";
        } else {
            userService.updatePassword(user, password);
            model.addAttribute("message", "You have successfully changed your password.");
        }
        return "login";
    }
}