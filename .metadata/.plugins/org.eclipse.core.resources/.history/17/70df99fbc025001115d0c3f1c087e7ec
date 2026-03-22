package com.sss.cartnest.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sss.cartnest.entities.User;

public interface UserRepository extends JpaRepository<User,Integer> {
	
	
	Optional<User> findByEmail(String email);
	
	Optional<User> findByUsername(String username);
}
