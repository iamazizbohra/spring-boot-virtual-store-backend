package com.coedmaster.vstore.domain.specification;

import org.springframework.data.jpa.domain.Specification;

import com.coedmaster.vstore.domain.Order;
import com.coedmaster.vstore.enums.OrderStatus;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class OrderSpecs {

	public static Specification<Order> hasOrderId(Long orderId) {
		return (Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> criteriaBuilder
				.equal(root.get("id"), orderId);
	}

	public static Specification<Order> hasUserId(Long userId) {
		return (Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> criteriaBuilder
				.equal(root.get("user").get("id"), userId);
	}

	public static Specification<Order> hasStoreId(Long storeId) {
		return (Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> criteriaBuilder
				.equal(root.get("store").get("id"), storeId);
	}

	public static Specification<Order> hasStatus(OrderStatus status) {
		return (Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> criteriaBuilder
				.equal(root.get("status"), status);
	}

}
