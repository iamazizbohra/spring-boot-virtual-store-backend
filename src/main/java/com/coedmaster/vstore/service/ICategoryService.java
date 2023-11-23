package com.coedmaster.vstore.service;

import java.util.List;

import com.coedmaster.vstore.dto.UpdateStatusDto;
import com.coedmaster.vstore.dto.request.CategoryRequestDto;
import com.coedmaster.vstore.model.Category;

public interface ICategoryService {
	List<Category> getCategories();

	Category createCategory(CategoryRequestDto payload);

	Category updateCategory(Long id, CategoryRequestDto payload);

	void deleteCategory(Long id);

	Category updateCategoryStatus(Long id, UpdateStatusDto payload);
}
