package com.sss.cartnest.adminservices;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sss.cartnest.entities.Order;
import com.sss.cartnest.entities.OrderItem;
import com.sss.cartnest.repositories.OrderItemRepository;
import com.sss.cartnest.repositories.OrderRepository;
import com.sss.cartnest.repositories.ProductRepository;

@Service
public class AdminBusinessService {

	@Autowired
	private OrderRepository order_repo;
	@Autowired
	private OrderItemRepository order_item_repo;
	@Autowired
	private ProductRepository prod_repo;

	// ================= BY MONTH =================
	public Map<String, Object> calculateMonthlyBusiness(int month, int year) {

		if (month < 1 || month > 12)
			throw new IllegalArgumentException("Invalid month: " + month);
		if (year < 2000 || year > 2100)
			throw new IllegalArgumentException("Invalid year: " + year);

		List<Order> successfulOrders = order_repo.findSuccessfulOrdersByMonthAndYear(month, year);
		return buildReport(successfulOrders);
	}

	// ================= BY YEAR =================
	public Map<String, Object> calculateYearlyBusiness(int year) {

		if (year < 2000 || year > 2100)
			throw new IllegalArgumentException("Invalid year: " + year);

		List<Order> successfulOrders = order_repo.findSuccessfulOrdersByYear(year);
		return buildReport(successfulOrders);
	}

	// ================= BY DAY =================
	public Map<String, Object> calculateDailyBusiness(LocalDate date) {

		if (date == null)
			throw new IllegalArgumentException("Date cannot be null");

		List<Order> successfulOrders = order_repo.findSuccessfulOrdersByDate(date);
		return buildReport(successfulOrders);
	}

	// ================= OVERALL =================
	public Map<String, Object> calculateOverallBusiness() {

		BigDecimal totalBusiness = order_repo.calculateOverallBusiness();

		Map<String, Object> businessReport = new HashMap<>();
		businessReport.put("totalBusiness", totalBusiness != null ? totalBusiness.doubleValue() : 0.0);
		return businessReport;
	}

	// ================= HELPER =================
	private Map<String, Object> buildReport(List<Order> orders) {

		double totalBusiness = 0.0;
		Map<String, Integer> categorySales = new HashMap<>();

		for (Order order : orders) {
			totalBusiness += order.getTotalAmount().doubleValue();

			List<OrderItem> orderItems = order_item_repo.findByOrderId(order.getOrderId());
			for (OrderItem item : orderItems) {
				String categoryName = prod_repo.findCategoryNameByProductId(item.getProductId());
				if (categoryName != null) {
					categorySales.put(
						categoryName,
						categorySales.getOrDefault(categoryName, 0) + item.getQuantity()
					);
				}
			}
		}

		Map<String, Object> businessReport = new HashMap<>();
		businessReport.put("totalBusiness", totalBusiness);
		businessReport.put("categorySales", categorySales);
		businessReport.put("totalOrders", orders.size());

		return businessReport;
	}
}