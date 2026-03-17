package com.sss.cartnest.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sss.cartnest.entities.CartItems;

import jakarta.transaction.Transactional;

public interface CartRepository extends JpaRepository<CartItems, Integer> {

	// Fetch cart item for a given userId and productId
	@Query("SELECT c FROM CartItems c WHERE c.user.user_id = :userId AND c.product.productId = :productId")
	Optional<CartItems> findByUserAndProduct(@Param("userId") int userId, @Param("productId") int productId);

	@Query("SELECT c FROM CartItems c JOIN FETCH c.product p WHERE c.user.user_id = :userId")
	List<CartItems> findCartItemsWithProductDetails(@Param("userId") int userId);

	// Update quantity for a specific cart item
	@Modifying
	@Transactional
	@Query("UPDATE CartItems c SET c.quantity = :quantity WHERE c.id = :cartItemId")
	void updateCartItemQuantity(@Param("cartItemId") int cartItemId, @Param("quantity") int quantity);

	// Delete a product from the cart
	@Modifying
	@Transactional
	@Query("DELETE FROM CartItems c WHERE c.user.user_id = :userId AND c.product.productId = :productId")
	void deleteCartItem(@Param("userId") int userId, @Param("productId") int productId);

	// Count the total quantity of items in the cart
	@Query("SELECT COALESCE(SUM(c.quantity), 0) FROM CartItems c WHERE c.user.user_id = :userId")
	int countTotalItems(@Param("userId") int userId);

	@Modifying
	@Transactional
	@Query("DELETE FROM CartItems c WHERE c.user.user_id = :userId")
	void deleteAllCartItemsByUserId(@Param("userId") int userId);
}