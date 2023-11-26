package com.coedmaster.vstore.respository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.coedmaster.vstore.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
	Optional<Category> findByIdAndStoreId(Long id, Long storeId);

	List<Category> findAllByStoreId(Long id, Sort sort);

	List<Category> findAllByIdInAndStoreId(List<Long> ids, Long storeId, Sort sort);
}
