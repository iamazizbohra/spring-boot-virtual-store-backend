package com.coedmaster.vstore.respository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.coedmaster.vstore.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	Optional<Product> findByIdAndStoreId(Long id, Long storeId);

	Page<Product> findAllByStoreId(Long id, Pageable pageable);

	Page<Product> findAllByStoreIdAndCategoryId(Long id, Long categoryId, Pageable pageable);
}
