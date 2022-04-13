package net.knowledgebase.springboot.service;

import net.knowledgebase.springboot.model.User;
import net.knowledgebase.springboot.web.dto.UserRegistrationDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService{
	User save(UserRegistrationDto registrationDto);

	List<User> getAllUsers();

	User deleteUserById(Long id);

	User getUserById(Long id);

	User updateUserPassword(User user);
}
