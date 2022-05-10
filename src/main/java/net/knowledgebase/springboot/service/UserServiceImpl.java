package net.knowledgebase.springboot.service;

import net.bytebuddy.utility.RandomString;
import net.knowledgebase.springboot.exception.UserNotFoundException;
import net.knowledgebase.springboot.model.Role;
import net.knowledgebase.springboot.model.Settings;
import net.knowledgebase.springboot.model.User;
import net.knowledgebase.springboot.repository.SettingsRepository;
import net.knowledgebase.springboot.repository.UserRepository;
import net.knowledgebase.springboot.util.Utility;
import net.knowledgebase.springboot.web.dto.UserRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private SettingsRepository settingsRepository;
    private SmtpService smtpService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, SettingsRepository settingsRepository, SmtpService smtpService) {
        super();
        this.userRepository = userRepository;
        this.settingsRepository = settingsRepository;
        this.smtpService = smtpService;
    }

    public Settings getSettings() {
        List<Settings> databaseSettings = settingsRepository.findAll();
        Settings settings = new Settings();
        settings.setId(databaseSettings.get(0).getId());
        settings.setUrl(databaseSettings.get(0).getUrl());
        settings.setEmail(databaseSettings.get(0).isEmail());
        return settings;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User save(UserRegistrationDto registrationDto, HttpServletRequest request) {
        User blankUser = new User();
        try {
            Settings settings = getSettings();
            String token = RandomString.make(30);
            String resetPasswordLink = settings.getUrl() + "/reset_password?token=" + token;
            String clientContent = "Hello,"
                    + "\n\nA new account has been created for you on the Snow Technology Knowledgebase."
                    + " Click the link below to set your password: \n\n"
                    + resetPasswordLink
                    + "\n\nThe Knowledge Base is the new location to find help content for the Quantum Payroll application."
                    + "\n\nDownloads will also now exclusively be available through the Knowledge Base"
                    + "\n\nIf you have any queries, please contact our support team";
            String otherContent = "Hello,"
                    + "\n\nA new account has been created for you on the Snow Technology Knowledgebase."
                    + " Click the link below to set your password: \n\n"
                    + resetPasswordLink;
            if (registrationDto.getRole().equals("User")) {
                User user = new User(registrationDto.getFirstName(),
                        registrationDto.getLastName(), registrationDto.getEmail(),
                        null, registrationDto.getRole(), Arrays.asList(new Role("ROLE_USER")), token);
                if (settings.isEmail() == true) {
                    smtpService.sendEmail(user.getEmail(), "Snow Knowledge Base Account Creation", otherContent);
                }
                return userRepository.save(user);
            } else if (registrationDto.getRole().equals("Admin")) {
                User user = new User(registrationDto.getFirstName(),
                        registrationDto.getLastName(), registrationDto.getEmail(),
                        null, registrationDto.getRole(), Arrays.asList(new Role("ROLE_ADMIN")), token);
                if (settings.isEmail() == true) {
                    smtpService.sendEmail(user.getEmail(), "Snow Knowledge Base Account Creation", otherContent);
                }
                return userRepository.save(user);
            } else if (registrationDto.getRole().equals("Client")) {
                User user = new User(registrationDto.getFirstName(),
                        registrationDto.getLastName(), registrationDto.getEmail(),
                        null, registrationDto.getRole(), Arrays.asList(new Role("ROLE_CLIENT")), token);
                if (settings.isEmail() == true) {
                    smtpService.sendEmail(user.getEmail(), "Snow Knowledge Base Account Creation", clientContent);
                }
                return userRepository.save(user);
            }
        } catch (Exception e) {
            return blankUser;
        }
        return blankUser;
    }

    @Override
    public User updateUserPassword(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    @Override
    public User deleteUserById(Long id) {
        userRepository.deleteById(id);
        return null;
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public void updateResetPasswordToken(String token, String email) throws UserNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.setResetPasswordToken(token);
            userRepository.save(user);
        } else {
            throw new UserNotFoundException("Could not find any user with the email " + email);
        }
    }

    @Override
    public User getByResetPasswordToken(String token) {
        return userRepository.findByResetPasswordToken(token);
    }

    @Override
    public void updatePassword(User user, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        user.setResetPasswordToken(null);
        userRepository.save(user);
    }

}
