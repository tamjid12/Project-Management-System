package net.javaguides.springboot.service;
/**
 * Author: Akid Tamjid Rahman
 */
import net.javaguides.springboot.model.Project;
import org.springframework.security.core.userdetails.UserDetailsService;

import net.javaguides.springboot.model.User;
import net.javaguides.springboot.web.dto.UserRegistrationDto;

import java.util.List;

public interface UserService extends UserDetailsService{
	User save(UserRegistrationDto registrationDto);
	User getUserById(Long userId);
	List<User> getAllUsers();

	User findByEmail(String email);
}
