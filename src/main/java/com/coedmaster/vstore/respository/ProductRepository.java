package com.coedmaster.vstore.respository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.coedmaster.vstore.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	Optional<Product> findByIdAndStoreId(Long id, Long storeId);

	List<Product> findAllByStoreId(Long id, Sort sort);
}
