package com.coedmaster.vstore.service;

import java.util.List;

import com.coedmaster.vstore.dto.UpdateStatusDto;
import com.coedmaster.vstore.dto.request.ProductRequestDto;
import com.coedmaster.vstore.model.Product;
import com.coedmaster.vstore.model.Store;

public interface IProductService {
	Product getProduct(Long id, Store store);

	List<Product> getProducts(Store store);

	Product createProduct(Store store, ProductRequestDto payload);

	Product updateProduct(Long id, Store store, ProductRequestDto payload);

	void deleteProduct(Long id, Store store);

	Product updateProductStatus(Long id, Store store, UpdateStatusDto payload);
}
