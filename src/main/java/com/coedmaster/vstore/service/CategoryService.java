package com.coedmaster.vstore.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.coedmaster.vstore.dto.UpdateStatusDto;
import com.coedmaster.vstore.dto.request.CategoryRequestDto;
import com.coedmaster.vstore.exception.EntityNotFoundException;
import com.coedmaster.vstore.model.Category;
import com.coedmaster.vstore.model.Store;
import com.coedmaster.vstore.respository.CategoryRepository;

@Service
public class CategoryService implements ICategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public Category getCategory(Long id, Store store) {
		return categoryRepository.findByIdAndStoreId(id, store.getId())
				.orElseThrow(() -> new EntityNotFoundException("Category not found"));
	}

	@Override
	public List<Category> getCategories(Store store) {
		return categoryRepository.findAllByStoreId(store.getId(), Sort.by("name"));
	}

	@Override
	public List<Category> getCategories(List<Long> ids, Store store) {
		return categoryRepository.findAllByIdInAndStoreId(ids, store.getId(), Sort.by("name"));
	}

	@Override
	public Category createCategory(Store store, CategoryRequestDto payload) {
		Category category = new Category();
		category.setStore(store);
		category.setName(payload.getName());
		category.setImage(payload.getImage());
		category.setEnabled(true);

		return categoryRepository.save(category);
	}

	@Override
	public Category updateCategory(Long id, Store store, CategoryRequestDto payload) {
		Category category = getCategory(id, store);
		category.setName(payload.getName());
		category.setImage(payload.getImage());

		return categoryRepository.save(category);
	}

	@Override
	public void deleteCategory(Long id, Store store) {
		Category category = getCategory(id, store);

		categoryRepository.deleteById(category.getId());
	}

	@Override
	public Category updateCategoryStatus(Long id, Store store, UpdateStatusDto payload) {
		Category category = getCategory(id, store);
		category.setEnabled(payload.isEnabled());

		return categoryRepository.save(category);
	}

}
