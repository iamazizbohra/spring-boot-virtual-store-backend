package com.coedmaster.vstore.service;

import java.util.List;

import com.coedmaster.vstore.dto.CategoryDto;
import com.coedmaster.vstore.dto.UpdateStatusDto;
import com.coedmaster.vstore.model.Category;
import com.coedmaster.vstore.model.Store;

public interface ICategoryService {
	Category getCategory(Long categoryId, Store store);

	List<Category> getCategories(Store store);

	List<Category> getCategories(List<Long> categoryIds, Store store);

	Category createCategory(Store store, CategoryDto payload);

	Category updateCategory(Long categoryId, Store store, CategoryDto payload);

	void deleteCategory(Long categoryId, Store store);

	Category updateCategoryStatus(Long categoryId, Store store, UpdateStatusDto payload);
}
