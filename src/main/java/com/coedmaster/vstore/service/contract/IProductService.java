package com.coedmaster.vstore.service.contract;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.coedmaster.vstore.dto.ProductDto;
import com.coedmaster.vstore.model.Category;
import com.coedmaster.vstore.model.Product;
import com.coedmaster.vstore.model.Store;

public interface IProductService {
	Product getProduct(Long productId, Store store);

	Page<Product> getProducts(Store store, Pageable pageable);

	Page<Product> getProducts(Store store, List<Category> categories, Pageable pageable);

	Product createProduct(Store store, ProductDto payload);

	Product updateProduct(Long productId, Store store, ProductDto payload);

	Product updateProductStatus(Long productId, Store store, Boolean status);

	Product updateProductQuantity(Long productId, Store store, Integer quantity);

	void deleteProduct(Long productId, Store store);
}
