package com.coedmaster.vstore.service;

import java.util.List;

import com.coedmaster.vstore.dto.UpdateStatusDto;
import com.coedmaster.vstore.dto.request.CategoryRequestDto;
import com.coedmaster.vstore.model.Category;
import com.coedmaster.vstore.model.Store;

public interface ICategoryService {
	Category getCategory(Long id, Store store);

	List<Category> getCategories(Store store);

	List<Category> getCategories(List<Long> ids, Store store);

	Category createCategory(Store store, CategoryRequestDto payload);

	Category updateCategory(Long id, Store store, CategoryRequestDto payload);

	void deleteCategory(Long id, Store store);

	Category updateCategoryStatus(Long id, Store store, UpdateStatusDto payload);
}
