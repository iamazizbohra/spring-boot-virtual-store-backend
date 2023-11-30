package com.coedmaster.vstore.service.contract;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.coedmaster.vstore.domain.Category;
import com.coedmaster.vstore.domain.Product;
import com.coedmaster.vstore.domain.Store;
import com.coedmaster.vstore.dto.ProductDto;

public interface IProductService {
	Product getProduct(Long productId, Store store);

	Product getProduct(Long productId, Store store, boolean enabled);

	Page<Product> getProducts(Store store, Pageable pageable);

	Page<Product> getProducts(Store store, boolean enabled, Pageable pageable);

	Page<Product> getProducts(Store store, List<Category> categories, Pageable pageable);

	Page<Product> getProducts(Store store, List<Category> categories, boolean enabled, Pageable pageable);

	Product createProduct(Store store, ProductDto payload);

	Product updateProduct(Long productId, Store store, ProductDto payload);

	Product updateProductStatus(Long productId, Store store, Boolean status);

	Product updateProductQuantity(Long productId, Store store, Integer quantity);

	void deleteProduct(Long productId, Store store);
}
