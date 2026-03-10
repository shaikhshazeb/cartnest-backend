package com.sss.cartnest.services;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.sss.cartnest.entities.User;
import com.sss.cartnest.repositories.UserRepository;

@Service
public class UserService {
	
	 private final UserRepository userRepo;
	 private final BCryptPasswordEncoder passEncode;

	    
	    public UserService(UserRepository userRepo) {
	        this.userRepo = userRepo;
	        this.passEncode = new BCryptPasswordEncoder();
	    }
	
	    
	// Sign Up    
	public User createUser(User user) {
		
		if(userRepo.findByUsername(user.getUsername()).isPresent()) {
			throw new RuntimeException("username is already exist");
		}
		if(userRepo.findByEmail(user.getEmail()).isPresent()) {
			throw new RuntimeException("email is already exist");
		}
		
		user.setPassword(passEncode.encode(user.getPassword()));
		
		return userRepo.save(user);
	}
}
