package com.coedmaster.vstore.service;

import java.util.List;

import com.coedmaster.vstore.dto.UpdateStatusDto;
import com.coedmaster.vstore.dto.request.ProductRequestDto;
import com.coedmaster.vstore.model.Product;

public interface IProductService {
	List<Product> getProducts();

	Product createProduct(ProductRequestDto payload);

	Product updateProduct(Long id, ProductRequestDto payload);

	void deleteProduct(Long id);

	Product updateProductStatus(Long id, UpdateStatusDto payload);
}
