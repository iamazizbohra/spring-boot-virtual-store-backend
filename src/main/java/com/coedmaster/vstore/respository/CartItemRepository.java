package com.coedmaster.vstore.respository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coedmaster.vstore.domain.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
	Optional<CartItem> findByIdAndCartId(Long cartItemId, Long cartId);

	Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);

	List<CartItem> findAllByCartId(Long cartId);
}
