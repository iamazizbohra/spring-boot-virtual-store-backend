package com.coedmaster.vstore.model.specification;

import org.springframework.data.jpa.domain.Specification;

import com.coedmaster.vstore.enums.OrderStatus;
import com.coedmaster.vstore.model.Order;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class OrderSpecs {

	public static Specification<Order> hasOrderId(Long orderId) {
		return (Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> builder
				.equal(root.get("id"), orderId);
	}

	public static Specification<Order> hasUserId(Long userId) {
		return (Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> builder
				.equal(root.get("user").get("id"), userId);
	}

	public static Specification<Order> hasStoreId(Long storeId) {
		return (Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> builder
				.equal(root.get("store").get("id"), storeId);
	}

	public static Specification<Order> hasStatus(OrderStatus status) {
		return (Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> builder
				.equal(root.get("status"), status);
	}

}
