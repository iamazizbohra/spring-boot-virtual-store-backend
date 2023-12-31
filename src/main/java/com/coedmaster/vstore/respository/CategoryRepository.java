package com.coedmaster.vstore.respository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.coedmaster.vstore.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
	Optional<Category> findByIdAndStoreId(Long categoryId, Long storeId);

	List<Category> findAllByStoreId(Long storeId, Sort sort);

	List<Category> findAllByStoreIdAndEnabled(Long storeId, Boolean enabled, Sort sort);

	List<Category> findAllByIdInAndStoreId(List<Long> categoryIds, Long storeId, Sort sort);

	List<Category> findAllByIdInAndStoreIdAndEnabled(List<Long> categoryIds, Long storeId, Boolean enabled, Sort sort);
}
