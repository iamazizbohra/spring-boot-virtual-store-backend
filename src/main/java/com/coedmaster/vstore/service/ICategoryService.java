package com.coedmaster.vstore.service;

import java.util.List;

import com.coedmaster.vstore.dto.UpdateStatusDto;
import com.coedmaster.vstore.dto.request.CategoryRequestDto;
import com.coedmaster.vstore.model.Category;
import com.coedmaster.vstore.model.Store;

public interface ICategoryService {
	Category getCategory(Long categoryId, Store store);

	List<Category> getCategories(Store store);

	List<Category> getCategories(List<Long> categoryIds, Store store);

	Category createCategory(Store store, CategoryRequestDto payload);

	Category updateCategory(Long categoryId, Store store, CategoryRequestDto payload);

	void deleteCategory(Long categoryId, Store store);

	Category updateCategoryStatus(Long categoryId, Store store, UpdateStatusDto payload);
}
