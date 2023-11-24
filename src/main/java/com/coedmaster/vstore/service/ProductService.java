package com.coedmaster.vstore.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.coedmaster.vstore.dto.UpdateStatusDto;
import com.coedmaster.vstore.dto.request.ProductRequestDto;
import com.coedmaster.vstore.exception.EntityNotFoundException;
import com.coedmaster.vstore.model.Product;
import com.coedmaster.vstore.model.Store;
import com.coedmaster.vstore.respository.ProductRepository;

@Service
public class ProductService implements IProductService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private StoreService storeService;

	@Override
	public List<Product> getProducts() {
		Store store = getStore();

		return productRepository.findAllByStoreId(store.getId(), Sort.by(Sort.Direction.DESC, "lastModifiedDate"));
	}

	@Override
	public Product createProduct(ProductRequestDto payload) {
		Store store = getStore();

		Product product = new Product();
		product.setStore(store);
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
	public Product updateProduct(Long id, ProductRequestDto payload) {
		Store store = getStore();

		Product product = getProduct(id, store);

		product.setName(payload.getName());
		product.setDescription(payload.getDescription());
		product.setImage(payload.getImage());
		product.setPrice(payload.getPrice());
		product.setOldPrice(payload.getOldPrice());
		product.setQuantity(payload.getQuantity());

		return productRepository.save(product);
	}

	@Override
	public void deleteProduct(Long id) {
		Store store = getStore();

		Product product = getProduct(id, store);

		productRepository.deleteById(product.getId());
	}

	@Override
	public Product updateProductStatus(Long id, UpdateStatusDto payload) {
		Store store = getStore();

		Product product = getProduct(id, store);

		product.setEnabled(payload.isEnabled());

		return productRepository.save(product);
	}

	private Product getProduct(Long id, Store store) {
		return productRepository.findByIdAndStoreId(id, store.getId())
				.orElseThrow(() -> new EntityNotFoundException("Product not found"));
	}

	private Store getStore() {
		return storeService.getStoreByAuthentication();
	}

}
