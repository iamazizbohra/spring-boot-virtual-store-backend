package com.coedmaster.vstore.controller.seller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RestController;

import com.coedmaster.vstore.dto.UpdateStatusDto;
import com.coedmaster.vstore.dto.request.ProductRequestDto;
import com.coedmaster.vstore.dto.response.ProductResponseDto;
import com.coedmaster.vstore.dto.response.SuccessResponseDto;
import com.coedmaster.vstore.model.Product;
import com.coedmaster.vstore.service.ProductService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

@RestController
@RequestMapping("/seller")
public class ProductController {
	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private Validator validator;

	@Autowired
	private ProductService productService;

	@GetMapping("/product")
	public ResponseEntity<SuccessResponseDto> getProducts(HttpServletRequest request) {
		List<Product> products = productService.getProducts();

		List<ProductResponseDto> productResponseDtos = products.stream()
				.map(e -> modelMapper.map(e, ProductResponseDto.class)).collect(Collectors.toList());

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Products fetched successful").data(productResponseDtos).path(request.getServletPath())
				.build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}

	@PostMapping("/product")
	public ResponseEntity<SuccessResponseDto> createProduct(HttpServletRequest request,
			@RequestBody ProductRequestDto payload) {
		Set<ConstraintViolation<ProductRequestDto>> violations = validator.validate(payload);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException("Constraint violation", violations);
		}

		Product product = productService.createProduct(payload);

		ProductResponseDto productResponseDto = modelMapper.map(product, ProductResponseDto.class);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Product created successful").data(productResponseDto).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}

	@PutMapping("/product/{id}")
	public ResponseEntity<SuccessResponseDto> updateProduct(HttpServletRequest request,
			@PathVariable(value = "id") Long id, @RequestBody ProductRequestDto payload) {
		Set<ConstraintViolation<ProductRequestDto>> violations = validator.validate(payload);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException("Constraint violation", violations);
		}

		Product product = productService.updateProduct(id, payload);

		ProductResponseDto productResponseDto = modelMapper.map(product, ProductResponseDto.class);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Product updated successful").data(productResponseDto).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}

	@DeleteMapping("/product/{id}")
	public ResponseEntity<SuccessResponseDto> deleteProduct(HttpServletRequest request,
			@PathVariable(value = "id") Long id) {

		productService.deleteProduct(id);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Product deleted successful").data(null).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}

	@PatchMapping("/product/{id}/status")
	public ResponseEntity<SuccessResponseDto> updateProductStatus(HttpServletRequest request,
			@PathVariable(value = "id") Long id, @RequestBody UpdateStatusDto payload) {
		Set<ConstraintViolation<UpdateStatusDto>> violations = validator.validate(payload);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException("Constraint violation", violations);
		}

		Product product = productService.updateProductStatus(id, payload);

		ProductResponseDto productResponseDto = modelMapper.map(product, ProductResponseDto.class);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Product status updated successful").data(productResponseDto).path(request.getServletPath())
				.build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}

}
