package net.knowledgebase.springboot.service;

import net.knowledgebase.springboot.model.User;
import net.knowledgebase.springboot.web.dto.UserRegistrationDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserService extends UserDetailsService{
	User save(UserRegistrationDto registrationDto, HttpServletRequest request);

	List<User> getAllUsers();

	User deleteUserById(Long id);

	User getUserById(Long id);

	User updateUserPassword(User user);

	void updateResetPasswordToken(String token, String email);

	User getByResetPasswordToken(String token);

	void updatePassword(User user, String newPassword);
}
