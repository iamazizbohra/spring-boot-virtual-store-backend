package com.coedmaster.vstore.respository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.coedmaster.vstore.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
	Optional<Order> findByIdAndUserId(Long orderId, Long userId);

	Optional<Order> findByIdAndStoreId(Long orderId, Long storeId);

	Page<Order> findAllByUserId(Long userId, Pageable pageable);

	Page<Order> findAllByStoreId(Long storeId, Pageable pageable);

	Page<Order> findAll(Specification<Order> spec, Pageable pageable);
}
