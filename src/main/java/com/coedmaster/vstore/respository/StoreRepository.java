package com.coedmaster.vstore.respository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coedmaster.vstore.model.Store;

public interface StoreRepository extends JpaRepository<Store, Long> {
	Optional<Store> findByUserId(Long id);
	
	Optional<Store> findByCode(String code);
}
