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
import com.coedmaster.vstore.dto.request.CategoryRequestDto;
import com.coedmaster.vstore.dto.response.CategoryResponseDto;
import com.coedmaster.vstore.dto.response.SuccessResponseDto;
import com.coedmaster.vstore.model.Category;
import com.coedmaster.vstore.model.Store;
import com.coedmaster.vstore.service.AuthenticationService;
import com.coedmaster.vstore.service.CategoryService;
import com.coedmaster.vstore.service.IStoreService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

@RestController
@RequestMapping("/seller")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private IStoreService storeService;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private Validator validator;
	
	@GetMapping("/category/{id}")
	public ResponseEntity<SuccessResponseDto> getCategory(HttpServletRequest request,
			@PathVariable(value = "id") Long id) {
		Store store = storeService
				.getStoreByUser(authenticationService.getAuthenticatedUser(authenticationService.getAuthentication()));

		Category category = categoryService.getCategory(id, store);

		CategoryResponseDto categoryResponseDto = modelMapper.map(category, CategoryResponseDto.class);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Category fetched successfully").data(categoryResponseDto).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}

	@GetMapping("/category")
	public ResponseEntity<SuccessResponseDto> getCategories(HttpServletRequest request) {
		Store store = storeService
				.getStoreByUser(authenticationService.getAuthenticatedUser(authenticationService.getAuthentication()));

		List<Category> categories = categoryService.getCategories(store);

		List<CategoryResponseDto> categoryResponseDtos = categories.stream()
				.map(e -> modelMapper.map(e, CategoryResponseDto.class)).collect(Collectors.toList());

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Categories fetched successfully").data(categoryResponseDtos).path(request.getServletPath())
				.build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}

	@PostMapping("/category")
	public ResponseEntity<SuccessResponseDto> createCategory(HttpServletRequest request,
			@RequestBody CategoryRequestDto payload) {
		Set<ConstraintViolation<CategoryRequestDto>> violations = validator.validate(payload);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException("Constraint violation", violations);
		}

		Store store = storeService
				.getStoreByUser(authenticationService.getAuthenticatedUser(authenticationService.getAuthentication()));

		Category category = categoryService.createCategory(store, payload);

		CategoryResponseDto categoryResponseDto = modelMapper.map(category, CategoryResponseDto.class);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Category created successfully").data(categoryResponseDto).path(request.getServletPath())
				.build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);

	}

	@PutMapping("/category/{id}")
	public ResponseEntity<SuccessResponseDto> updateCategory(HttpServletRequest request,
			@PathVariable(value = "id") Long id, @RequestBody CategoryRequestDto payload) {
		Set<ConstraintViolation<CategoryRequestDto>> violations = validator.validate(payload);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException("Constraint violation", violations);
		}

		Store store = storeService
				.getStoreByUser(authenticationService.getAuthenticatedUser(authenticationService.getAuthentication()));

		Category category = categoryService.updateCategory(id, store, payload);

		CategoryResponseDto categoryResponseDto = modelMapper.map(category, CategoryResponseDto.class);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Category updated successfully").data(categoryResponseDto).path(request.getServletPath())
				.build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}

	@DeleteMapping("/category/{id}")
	public ResponseEntity<SuccessResponseDto> deleteCategory(HttpServletRequest request,
			@PathVariable(value = "id") Long id) {

		Store store = storeService
				.getStoreByUser(authenticationService.getAuthenticatedUser(authenticationService.getAuthentication()));

		categoryService.deleteCategory(id, store);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Category deleted successfully").data(null).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}

	@PatchMapping("/category/{id}/status")
	public ResponseEntity<SuccessResponseDto> updateCategoryStatus(HttpServletRequest request,
			@PathVariable(value = "id") Long id, @RequestBody UpdateStatusDto payload) {
		Set<ConstraintViolation<UpdateStatusDto>> violations = validator.validate(payload);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException("Constraint violation", violations);
		}

		Store store = storeService
				.getStoreByUser(authenticationService.getAuthenticatedUser(authenticationService.getAuthentication()));

		Category category = categoryService.updateCategoryStatus(id, store, payload);

		CategoryResponseDto categoryResponseDto = modelMapper.map(category, CategoryResponseDto.class);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Category status updated successfully").data(categoryResponseDto).path(request.getServletPath())
				.build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}

}
