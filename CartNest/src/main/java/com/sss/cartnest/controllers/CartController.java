package com.sss.cartnest.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sss.cartnest.entities.User;
import com.sss.cartnest.repositories.UserRepository;
import com.sss.cartnest.services.CartService;

@RestController
@RequestMapping("/api/cart")
public class CartController {

	@Autowired
	private CartService cartService;

	@Autowired
	private UserRepository userRepository;

	// Get cart item count
	@GetMapping("/items/count")
	public ResponseEntity<Integer> getCartItemCount(@RequestParam String username) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
		int count = cartService.getCartItemCount(user.getUser_id());
		return ResponseEntity.ok(count);
	}

	// Get all cart items
	@GetMapping("/items")
	public ResponseEntity<Map<String, Object>> getCartItems(@RequestParam String username) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
		Map<String, Object> cartItems = cartService.getCartItems(user.getUser_id());
		return ResponseEntity.ok(cartItems);
	}

	// Add item to cart
	@PostMapping("/add")
	public ResponseEntity<Void> addToCart(@RequestBody Map<String, Object> request) {
		String username = (String) request.get("username");
		int productId = (int) request.get("productId");
		int quantity = request.containsKey("quantity") ? (int) request.get("quantity") : 1;

		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

		cartService.addToCart(user.getUser_id(), productId, quantity);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	// Update cart item quantity
	@PutMapping("/update")
	public ResponseEntity<Void> updateCartItemQuantity(@RequestBody Map<String, Object> request) {
		String username = (String) request.get("username");
		int productId = (int) request.get("productId");
		int quantity = (int) request.get("quantity");

		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

		cartService.updateCartItemsQuantity(user.getUser_id(), productId, quantity);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	// Delete cart item
	@DeleteMapping("/delete")
	public ResponseEntity<Void> deleteCartItem(@RequestBody Map<String, Object> request) {
		String username = (String) request.get("username");
		int productId = (int) request.get("productId");

		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

		cartService.deleteCartItem(user.getUser_id(), productId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

}