package com.coedmaster.vstore.model.specification;

import org.springframework.data.jpa.domain.Specification;

import com.coedmaster.vstore.model.Store;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class StoreSpecs {
	
	public static Specification<Store> hasStoreId(Long storeId) {
		return (Root<Store> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> builder.equal(root.get("id"),
				storeId);
	}

	public static Specification<Store> hasUserId(Long userId) {
		return (Root<Store> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> builder
				.equal(root.get("user").get("id"), userId);
	}

	public static Specification<Store> hasCode(String code) {
		return (Root<Store> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> builder.equal(root.get("code"),
				code);
	}

	public static Specification<Store> isEnabled(Boolean enabled) {
		return (Root<Store> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> builder.equal(root.get("enabled"),
				enabled);
	}
}
