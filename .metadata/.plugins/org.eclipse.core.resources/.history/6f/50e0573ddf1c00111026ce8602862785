package com.sss.cartnest.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sss.cartnest.dto.LoginRequest;
import com.sss.cartnest.entities.User;
import com.sss.cartnest.services.AuthService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "https://cartnest-sigma.vercel.app", allowCredentials = "true")
public class AuthControllers {
	
	@Autowired
	private AuthService authServ;
	
	@SuppressWarnings("rawtypes")
	@PostMapping("/login")
	public ResponseEntity login(@RequestBody LoginRequest req, HttpServletResponse resp) {
		
		try {
		User user	= authServ.loginAuth(req.getEmail(), req.getPassword());
		String token = authServ.generateToken(user);
		
		// Create a cookies
		Cookie cookie = new Cookie("authToken", token);

		cookie.setHttpOnly(true);
		cookie.setSecure(true);        // production me true
		cookie.setPath("/");
		cookie.setMaxAge(3600);

		resp.addCookie(cookie);
		
		Map<String, Object> responseBody = new HashMap<>();
		responseBody.put("message", "Login successful");
		responseBody.put("role", user.getRole().name());
		responseBody.put("username", user.getUsername());

		return ResponseEntity.ok(responseBody);
		
		
		} catch (Exception e) {
			 return ResponseEntity
			            .status(HttpStatus.UNAUTHORIZED)
			            .body(Map.of("error", e.getMessage()));
		}
	}
}
