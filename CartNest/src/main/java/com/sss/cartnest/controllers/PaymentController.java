package com.sss.cartnest.controllers;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.razorpay.RazorpayException;
import com.sss.cartnest.entities.User;
import com.sss.cartnest.repositories.UserRepository;
import com.sss.cartnest.services.PaymentService;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private UserRepository userRepository;

	// Create Razorpay Order
	@PostMapping("/create")
	public ResponseEntity<String> createPaymentOrder(@RequestBody Map<String, Object> requestBody) {
		try {
			String username = (String) requestBody.get("username");
			BigDecimal totalAmount = new BigDecimal(requestBody.get("totalAmount").toString());

			User user = userRepository.findByUsername(username)
					.orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

			String razorpayOrderId = paymentService.createOrder(user.getUser_id(), totalAmount);

			return ResponseEntity.ok(razorpayOrderId);

		} catch (RazorpayException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating Razorpay order");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request: " + e.getMessage());
		}
	}

	// Verify Payment
	@PostMapping("/verify")
	public ResponseEntity<String> verifyPayment(@RequestBody Map<String, Object> requestBody) {
		try {
			String username = (String) requestBody.get("username");
			String razorpayOrderId = (String) requestBody.get("razorpayOrderId");
			String razorpayPaymentId = (String) requestBody.get("razorpayPaymentId");
			String razorpaySignature = (String) requestBody.get("razorpaySignature");

			User user = userRepository.findByUsername(username)
					.orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

			boolean isVerified = paymentService.verifyPayment(
					razorpayOrderId,
					razorpayPaymentId,
					razorpaySignature,
					user.getUser_id()
			);

			if (isVerified) {
				return ResponseEntity.ok("Payment verified successfully");
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment verification failed");
			}

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error verifying payment");
		}
	}
}