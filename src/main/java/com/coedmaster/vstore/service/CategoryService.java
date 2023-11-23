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

	@Autowired
	private StoreService storeServiceImpl;

	@Override
	public List<Category> getCategories() {
		Store store = getStore();

		return categoryRepository.findAllByStoreId(store.getId(), Sort.by(Sort.Direction.ASC, "title"));
	}

	@Override
	public Category createCategory(CategoryRequestDto payload) {
		Store store = getStore();

		Category banner = Category.builder().store(store).title(payload.getTitle()).image(payload.getImage())
				.enabled(true).build();

		return categoryRepository.save(banner);
	}

	@Override
	public Category updateCategory(Long id, CategoryRequestDto payload) {
		Category category = getCategory(id);

		category.setTitle(payload.getTitle());
		category.setImage(payload.getImage());

		return categoryRepository.save(category);
	}

	@Override
	public void deleteCategory(Long id) {
		Category category = getCategory(id);

		categoryRepository.deleteById(category.getId());
	}

	@Override
	public Category updateCategoryStatus(Long id, UpdateStatusDto payload) {
		Category category = getCategory(id);

		category.setEnabled(payload.isEnabled());

		return categoryRepository.save(category);
	}

	private Category getCategory(Long id) {
		Store store = getStore();

		Category category = categoryRepository.findByIdAndStoreId(id, store.getId())
				.orElseThrow(() -> new EntityNotFoundException("Banner not found"));

		return category;
	}

	private Store getStore() {
		return storeServiceImpl.getStoreByAuthentication();
	}

}
