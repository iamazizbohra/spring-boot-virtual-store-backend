package com.coedmaster.vstore.controller.seller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.coedmaster.vstore.dto.ProductDto;
import com.coedmaster.vstore.dto.UpdateStatusDto;
import com.coedmaster.vstore.dto.response.SuccessResponseDto;
import com.coedmaster.vstore.model.Category;
import com.coedmaster.vstore.model.Product;
import com.coedmaster.vstore.model.Store;
import com.coedmaster.vstore.service.contract.IAuthenticationService;
import com.coedmaster.vstore.service.contract.ICategoryService;
import com.coedmaster.vstore.service.contract.IProductService;
import com.coedmaster.vstore.service.contract.IStoreService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

@RestController
@RequestMapping("/seller")
public class ProductController {

	@Autowired
	private IAuthenticationService authenticationService;

	@Autowired
	private IStoreService storeService;

	@Autowired
	private ICategoryService categoryService;

	@Autowired
	private IProductService productService;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private Validator validator;

	@GetMapping("/product/{productId}")
	public ResponseEntity<SuccessResponseDto> getProduct(HttpServletRequest request,
			@PathVariable(name = "productId") Long productId) {
		Store store = storeService
				.getStore(authenticationService.getAuthenticatedUser(authenticationService.getAuthentication()));

		Product product = productService.getProduct(productId, store);

		ProductDto productDto = modelMapper.map(product, ProductDto.class);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Product fetched successfully").data(productDto).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}

	@GetMapping("/product")
	public ResponseEntity<SuccessResponseDto> getProducts(HttpServletRequest request,
			@RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
			@RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
			@RequestParam(name = "categoryIds", defaultValue = "") List<Long> categoryIds,
			@RequestParam(name = "sortBy", defaultValue = "lastModifiedDate") String sortBy,
			@RequestParam(name = "sortDirection", defaultValue = "DESC") String sortDirection) {
		Store store = storeService
				.getStore(authenticationService.getAuthenticatedUser(authenticationService.getAuthentication()));

		PageRequest pageable = PageRequest.of(pageNumber, pageSize,
				Sort.by(Sort.Direction.valueOf(sortDirection), sortBy));

		Page<Product> productsPage;
		if (categoryIds.size() == 0) {
			productsPage = productService.getProducts(store, pageable);
		} else {
			List<Category> categories = categoryService.getCategories(categoryIds, store);
			productsPage = productService.getProducts(store, categories, pageable);
		}

		List<ProductDto> productDtos = productsPage.getContent().stream().map(e -> modelMapper.map(e, ProductDto.class))
				.collect(Collectors.toList());

		Map<String, Object> pageDetails = new HashMap<>();
		pageDetails.put("products", productDtos);
		pageDetails.put("currentPage", productsPage.getNumber());
		pageDetails.put("totalItems", productsPage.getTotalElements());
		pageDetails.put("totalPages", productsPage.getTotalPages());

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Products fetched successfully").data(pageDetails).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}

	@PostMapping("/product")
	public ResponseEntity<SuccessResponseDto> createProduct(HttpServletRequest request,
			@RequestBody ProductDto payload) {
		Set<ConstraintViolation<ProductDto>> violations = validator.validate(payload);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException("Constraint violation", violations);
		}

		Store store = storeService
				.getStore(authenticationService.getAuthenticatedUser(authenticationService.getAuthentication()));

		Product product = productService.createProduct(store, payload);

		ProductDto productDto = modelMapper.map(product, ProductDto.class);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Product created successfully").data(productDto).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}

	@PutMapping("/product/{productId}")
	public ResponseEntity<SuccessResponseDto> updateProduct(HttpServletRequest request,
			@PathVariable(name = "productId") Long productId, @RequestBody ProductDto payload) {
		Set<ConstraintViolation<ProductDto>> violations = validator.validate(payload);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException("Constraint violation", violations);
		}

		Store store = storeService
				.getStore(authenticationService.getAuthenticatedUser(authenticationService.getAuthentication()));

		Product product = productService.updateProduct(productId, store, payload);

		ProductDto productDto = modelMapper.map(product, ProductDto.class);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Product updated successfully").data(productDto).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}

	@PatchMapping("/product/{productId}/status")
	public ResponseEntity<SuccessResponseDto> updateProductStatus(HttpServletRequest request,
			@PathVariable(name = "productId") Long productId, @RequestBody UpdateStatusDto payload) {
		Set<ConstraintViolation<UpdateStatusDto>> violations = validator.validate(payload);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException("Constraint violation", violations);
		}

		Store store = storeService
				.getStore(authenticationService.getAuthenticatedUser(authenticationService.getAuthentication()));

		Product product = productService.updateProductStatus(productId, store, payload.isEnabled());

		ProductDto productDto = modelMapper.map(product, ProductDto.class);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Product status updated successfully").data(productDto).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}

	@DeleteMapping("/product/{productId}")
	public ResponseEntity<SuccessResponseDto> deleteProduct(HttpServletRequest request,
			@PathVariable(name = "productId") Long productId) {
		Store store = storeService
				.getStore(authenticationService.getAuthenticatedUser(authenticationService.getAuthentication()));

		productService.deleteProduct(productId, store);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Product deleted successfully").data(null).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}
}
