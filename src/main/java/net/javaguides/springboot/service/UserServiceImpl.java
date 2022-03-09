package net.javaguides.springboot.service;

import net.javaguides.springboot.model.Role;
import net.javaguides.springboot.model.User;
import net.javaguides.springboot.repository.UserRepository;
import net.javaguides.springboot.web.dto.UserRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository) {
        super();
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User save(UserRegistrationDto registrationDto) {
        User blankUser = new User();
        try {
            if (registrationDto.getRole().equals("User")) {
                User user = new User(registrationDto.getFirstName(),
                        registrationDto.getLastName(), registrationDto.getEmail(),
                        passwordEncoder.encode(registrationDto.getPassword()), registrationDto.getRole(), Arrays.asList(new Role("ROLE_USER")));
                return userRepository.save(user);
            } else if (registrationDto.getRole().equals("Admin")) {
                User user = new User(registrationDto.getFirstName(),
                        registrationDto.getLastName(), registrationDto.getEmail(),
                        passwordEncoder.encode(registrationDto.getPassword()), registrationDto.getRole(), Arrays.asList(new Role("ROLE_ADMIN")));
                return userRepository.save(user);
            }
        } catch (Exception e) {
            return blankUser;
        }
        return blankUser;
    }

    @Override
    public User updateUserPassword(User user){
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

}
