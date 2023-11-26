package com.coedmaster.vstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.coedmaster.vstore.dto.UpdateStatusDto;
import com.coedmaster.vstore.dto.request.ProductRequestDto;
import com.coedmaster.vstore.exception.EntityNotFoundException;
import com.coedmaster.vstore.model.Category;
import com.coedmaster.vstore.model.Product;
import com.coedmaster.vstore.model.Store;
import com.coedmaster.vstore.respository.ProductRepository;

@Service
public class ProductService implements IProductService {

	@Autowired
	private ProductRepository productRepository;

	@Override
	public Product getProduct(Long id, Store store) {
		return productRepository.findByIdAndStoreId(id, store.getId())
				.orElseThrow(() -> new EntityNotFoundException("Product not found"));
	}

	@Override
	public Page<Product> getProducts(Store store, Pageable pageable) {
		return productRepository.findAllByStoreId(store.getId(), pageable);
	}

	@Override
	public Product createProduct(Store store, Category category, ProductRequestDto payload) {
		Product product = new Product();
		product.setStore(store);
		product.setCategory(category);
		product.setName(payload.getName());
		product.setDescription(payload.getDescription());
		product.setImage(payload.getImage());
		product.setPrice(payload.getPrice());
		product.setOldPrice(payload.getOldPrice());
		product.setQuantity(payload.getQuantity());
		product.setEnabled(true);

		return productRepository.save(product);
	}

	@Override
	public Product updateProduct(Long id, Store store, Category category, ProductRequestDto payload) {
		Product product = getProduct(id, store);
		product.setCategory(category);
		product.setName(payload.getName());
		product.setDescription(payload.getDescription());
		product.setImage(payload.getImage());
		product.setPrice(payload.getPrice());
		product.setOldPrice(payload.getOldPrice());
		product.setQuantity(payload.getQuantity());

		return productRepository.save(product);
	}

	@Override
	public void deleteProduct(Long id, Store store) {
		Product product = getProduct(id, store);

		productRepository.deleteById(product.getId());
	}

	@Override
	public Product updateProductStatus(Long id, Store store, UpdateStatusDto payload) {
		Product product = getProduct(id, store);
		product.setEnabled(payload.isEnabled());

		return productRepository.save(product);
	}

}
