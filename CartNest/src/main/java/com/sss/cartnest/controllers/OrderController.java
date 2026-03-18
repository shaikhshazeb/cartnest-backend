package com.sss.cartnest.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sss.cartnest.entities.User;
import com.sss.cartnest.repositories.UserRepository;
import com.sss.cartnest.services.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

	@Autowired
	private OrderService orderService;

	@Autowired
	private UserRepository userRepository;

	@GetMapping
	public ResponseEntity<Map<String, Object>> getOrdersForUser(@RequestParam String username) {
		try {
			User user = userRepository.findByUsername(username)
					.orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

			Map<String, Object> response = orderService.getOrdersForUser(user);
			return ResponseEntity.ok(response);

		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body(Map.of("error", "An unexpected error occurred"));
		}
	}
}