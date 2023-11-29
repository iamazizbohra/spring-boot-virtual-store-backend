package com.coedmaster.vstore.respository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coedmaster.vstore.domain.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
	Optional<Cart> findByUserId(Long userId);
	
	Optional<Cart> findByUserIdAndStoreId(Long userId, Long storeId);
}
