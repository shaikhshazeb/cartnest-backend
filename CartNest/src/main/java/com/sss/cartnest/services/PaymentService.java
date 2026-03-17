package com.sss.cartnest.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sss.cartnest.entities.Order;
import com.sss.cartnest.entities.OrderItem;
import com.sss.cartnest.entities.OrderStatus;
import com.sss.cartnest.entities.CartItems;
import com.sss.cartnest.repositories.CartRepository;
import com.sss.cartnest.repositories.OrderItemRepository;
import com.sss.cartnest.repositories.OrderRepository;

import com.razorpay.RazorpayClient;
import org.json.JSONObject;

@Service
public class PaymentService {

	@Value("${razorpay.key_id}")
	private String razorpayKeyId;

	@Value("${razorpay.key_secret}")
	private String razorpayKeySecret;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;

	@Autowired
	private CartRepository cartRepository;

	// ================= CREATE ORDER =================
	@Transactional
	public String createOrder(int userId, BigDecimal totalAmount) throws Exception {

		RazorpayClient razorpayClient = new RazorpayClient(razorpayKeyId, razorpayKeySecret);

		JSONObject orderRequest = new JSONObject();
		orderRequest.put("amount", totalAmount.multiply(BigDecimal.valueOf(100)).intValue());
		orderRequest.put("currency", "INR");
		orderRequest.put("receipt", "txn_" + System.currentTimeMillis());

		com.razorpay.Order razorpayOrder = razorpayClient.orders.create(orderRequest);
		String razorpayOrderId = razorpayOrder.get("id");

		Order order = new Order();
		order.setOrderId(razorpayOrderId);
		order.setUserId(userId);
		order.setTotalAmount(totalAmount);
		order.setStatus(OrderStatus.PENDING);
		order.setCreated_at(LocalDateTime.now());
		order.setUpdated_at(LocalDateTime.now());

		orderRepository.save(order);

		return razorpayOrderId;
	}

	// ================= VERIFY PAYMENT =================
	@Transactional
	public boolean verifyPayment(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature,
			int userId) {

		try {
			JSONObject attributes = new JSONObject();
			attributes.put("razorpay_order_id", razorpayOrderId);
			attributes.put("razorpay_payment_id", razorpayPaymentId);
			attributes.put("razorpay_signature", razorpaySignature);

			boolean isSignatureValid = com.razorpay.Utils.verifyPaymentSignature(attributes, razorpayKeySecret);

			if (isSignatureValid) {

				Order order = orderRepository.findById(razorpayOrderId)
						.orElseThrow(() -> new RuntimeException("Order not found"));

				order.setStatus(OrderStatus.SUCCESS);
				order.setUpdated_at(LocalDateTime.now());
				orderRepository.save(order);

				// Cart items ko order items mein convert karo
				List<CartItems> cartItems = cartRepository.findCartItemsWithProductDetails(userId);

				for (CartItems cartItem : cartItems) {
					BigDecimal price = BigDecimal.valueOf(cartItem.getProduct().getPrice());
					BigDecimal totalPrice = price.multiply(BigDecimal.valueOf(cartItem.getQuantity()));

					OrderItem orderItem = new OrderItem(order, cartItem.getProduct().getProductId(),
							cartItem.getQuantity(), price, totalPrice);
					orderItemRepository.save(orderItem);
				}

				// Cart clear karo
				cartRepository.deleteAllCartItemsByUserId(userId);

				return true;
			}

			return false;

		} catch (Exception e) {
			throw new RuntimeException("Payment verification failed: " + e.getMessage(), e);
		}
	}
}