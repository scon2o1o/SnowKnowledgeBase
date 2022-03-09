package net.javaguides.springboot.service;

import net.javaguides.springboot.model.User;
import net.javaguides.springboot.web.dto.UserRegistrationDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService{
	User save(UserRegistrationDto registrationDto);

	List<User> getAllUsers();

	User deleteUserById(Long id);

	User getUserById(Long id);

	User updateUserPassword(User user);
}
