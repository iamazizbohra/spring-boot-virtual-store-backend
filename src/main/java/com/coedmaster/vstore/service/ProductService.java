package com.coedmaster.vstore.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.coedmaster.vstore.dto.ProductDto;
import com.coedmaster.vstore.exception.EntityNotFoundException;
import com.coedmaster.vstore.model.Category;
import com.coedmaster.vstore.model.Product;
import com.coedmaster.vstore.model.Store;
import com.coedmaster.vstore.respository.ProductRepository;
import com.coedmaster.vstore.service.contract.IProductService;

@Service
public class ProductService implements IProductService {

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ProductRepository productRepository;

	@Override
	public Product getProduct(Long productId, Store store) {
		return productRepository.findByIdAndStoreId(productId, store.getId())
				.orElseThrow(() -> new EntityNotFoundException("Product not found"));
	}

	@Override
	public Product getProduct(Long productId, Store store, boolean enabled) {
		return productRepository.findByIdAndStoreIdAndEnabled(productId, store.getId(), enabled)
				.orElseThrow(() -> new EntityNotFoundException("Product not found"));
	}

	@Override
	public Page<Product> getProducts(Store store, Pageable pageable) {
		return productRepository.findAllByStoreId(store.getId(), pageable);
	}

	@Override
	public Page<Product> getProducts(Store store, boolean enabled, Pageable pageable) {
		return productRepository.findAllByStoreIdAndEnabled(store.getId(), enabled, pageable);
	}

	@Override
	public Page<Product> getProducts(Store store, List<Category> categories, Pageable pageable) {
		List<Long> categoryIds = categories.stream().map((e) -> e.getId()).collect(Collectors.toList());

		return productRepository.findAllByStoreIdAndCategoryIdIn(store.getId(), categoryIds, pageable);
	}

	@Override
	public Page<Product> getProducts(Store store, List<Category> categories, boolean enabled, Pageable pageable) {
		List<Long> categoryIds = categories.stream().map((e) -> e.getId()).collect(Collectors.toList());

		return productRepository.findAllByStoreIdAndCategoryIdInAndEnabled(store.getId(), categoryIds, enabled,
				pageable);
	}

	@Override
	public Product createProduct(Store store, ProductDto payload) {
		Category category = categoryService.getCategory(payload.getCategoryId(), store);

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
	public Product updateProduct(Long productId, Store store, ProductDto payload) {
		Category category = categoryService.getCategory(payload.getCategoryId(), store);

		Product product = getProduct(productId, store);
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
	public Product updateProductStatus(Long productId, Store store, Boolean status) {
		Product product = getProduct(productId, store);
		product.setEnabled(status);

		return productRepository.save(product);
	}

	@Override
	public Product updateProductQuantity(Long productId, Store store, Integer quantity) {
		Product product = getProduct(productId, store);
		product.setQuantity(quantity);

		return productRepository.save(product);
	}

	@Override
	public void deleteProduct(Long productId, Store store) {
		Product product = getProduct(productId, store);

		productRepository.deleteById(product.getId());
	}

}
