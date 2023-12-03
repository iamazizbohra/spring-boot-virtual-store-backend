package com.coedmaster.vstore.respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coedmaster.vstore.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
	List<OrderItem> findAllByOrderId(Long orderId);
}
