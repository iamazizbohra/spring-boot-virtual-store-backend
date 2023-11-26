package com.coedmaster.vstore.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.coedmaster.vstore.dto.UpdateStatusDto;
import com.coedmaster.vstore.dto.request.ProductRequestDto;
import com.coedmaster.vstore.model.Category;
import com.coedmaster.vstore.model.Product;
import com.coedmaster.vstore.model.Store;

public interface IProductService {
	Product getProduct(Long productId, Store store);

	Page<Product> getProducts(Store store, Pageable pageable);
	
	Page<Product> getProducts(Store store, List<Category> categories, Pageable pageable);

	Product createProduct(Store store, Category category, ProductRequestDto payload);

	Product updateProduct(Long productId, Store store, Category category, ProductRequestDto payload);

	void deleteProduct(Long productId, Store store);

	Product updateProductStatus(Long productId, Store store, UpdateStatusDto payload);
}
