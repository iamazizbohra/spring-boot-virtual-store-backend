package com.coedmaster.vstore.respository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.coedmaster.vstore.model.Store;

public interface StoreRepository extends JpaRepository<Store, Long>, JpaSpecificationExecutor<Store> {
	Optional<Store> findByUserId(Long userId);

	Optional<Store> findByCode(String code);

	Optional<Store> findOne(Specification<Store> specs);

	Page<Store> findAll(Specification<Store> specs, Pageable pageable);
}
