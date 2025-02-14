package com.rmgYantra.loginapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rmgYantra.loginapp.exceptions.InvalidEmail;
import com.rmgYantra.loginapp.exceptions.InvalidMobileNumberException;
import com.rmgYantra.loginapp.model.User;
import com.rmgYantra.loginapp.repo.UserRepo;

@Service
public class UserService {
	@Autowired
	private UserRepo userRepo;
	
	@Autowired 
	private PasswordEncoder passwordEncoder;
	
	public User addUser(User user) {
		if (!user.getContact().matches("[0-9]{10}")) {
			throw new InvalidMobileNumberException("Enter a valid mobile number");
		} else if (!user.getEmail().matches(
				"^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z" + "A-Z]{2,7}$")) {
			throw new InvalidEmail("Enter a valid email ID");
		}
		String pw = user.getUsername().substring(1);
		System.out.println("Password: "+pw);
		user.setPassword(passwordEncoder.encode(pw));
		return userRepo.save(user);
		
		
	}

}
