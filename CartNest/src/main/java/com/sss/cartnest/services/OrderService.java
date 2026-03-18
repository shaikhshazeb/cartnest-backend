package com.sss.cartnest.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sss.cartnest.entities.OrderItem;
import com.sss.cartnest.entities.Product;
import com.sss.cartnest.entities.ProductImage;
import com.sss.cartnest.entities.User;
import com.sss.cartnest.repositories.OrderItemRepository;
import com.sss.cartnest.repositories.ProductImageRepository;
import com.sss.cartnest.repositories.ProductRepository;

@Service
public class OrderService {

	@Autowired
	private OrderItemRepository orderItemRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ProductImageRepository productImageRepository;

	public Map<String, Object> getOrdersForUser(User user) {
		List<OrderItem> orderItems = orderItemRepository.findSuccessfulOrderItemsByUserId(user.getUser_id());

		Map<String, Object> response = new HashMap<>();
		response.put("username", user.getUsername());
		response.put("role", user.getRole());

		List<Map<String, Object>> products = new ArrayList<>();

		for (OrderItem item : orderItems) {
			Product product = productRepository.findById(item.getProductId()).orElse(null);
			if (product == null) continue;

			List<ProductImage> images = productImageRepository.findByProduct_ProductId(product.getProductId());
			String imageUrl = images.isEmpty() ? null : images.get(0).getImage_url();

			Map<String, Object> productDetails = new HashMap<>();
			productDetails.put("order_id", item.getOrder().getOrderId());
			productDetails.put("status", item.getOrder().getStatus().toString());
			productDetails.put("quantity", item.getQuantity());
			productDetails.put("total_price", item.getTotalPrice());
			productDetails.put("image_url", imageUrl);
			productDetails.put("product_id", product.getProductId());
			productDetails.put("name", product.getName());
			productDetails.put("description", product.getDescription());
			productDetails.put("price_per_unit", item.getPricePerUnit());

			products.add(productDetails);
		}

		response.put("products", products);
		return response;
	}
}