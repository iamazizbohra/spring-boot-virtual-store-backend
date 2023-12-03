package com.coedmaster.vstore.respository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.coedmaster.vstore.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	Optional<Product> findByIdAndStoreId(Long productId, Long storeId);

	Optional<Product> findByIdAndStoreIdAndEnabled(Long productId, Long storeId, boolean enabled);

	Page<Product> findAllByStoreId(Long storeId, Pageable pageable);

	Page<Product> findAllByStoreIdAndEnabled(Long storeId, boolean enabled, Pageable pageable);

	Page<Product> findAllByStoreIdAndCategoryIdIn(Long storeId, List<Long> categoryIds, Pageable pageable);

	Page<Product> findAllByStoreIdAndCategoryIdInAndEnabled(Long storeId, List<Long> categoryIds, boolean enabled,
			Pageable pageable);
}
