package com.sss.cartnest.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sss.cartnest.entities.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

	@Query("SELECT oi FROM OrderItem oi WHERE oi.order.orderId = :orderId")
	List<OrderItem> findByOrderId(@Param("orderId") String orderId);

	@Query("SELECT oi FROM OrderItem oi WHERE oi.order.userId = :userId AND oi.order.status = 'SUCCESS'")
	List<OrderItem> findSuccessfulOrderItemsByUserId(@Param("userId") int userId);
}