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

import com.coedmaster.vstore.dto.CategoryDto;
import com.coedmaster.vstore.dto.UpdateStatusDto;
import com.coedmaster.vstore.dto.response.SuccessResponseDto;
import com.coedmaster.vstore.model.Category;
import com.coedmaster.vstore.model.Store;
import com.coedmaster.vstore.service.contract.IAuthenticationService;
import com.coedmaster.vstore.service.contract.ICategoryService;
import com.coedmaster.vstore.service.contract.IStoreService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

@RestController("SellerCategoryController")
@RequestMapping("/seller")
public class CategoryController {

	@Autowired
	private IAuthenticationService authenticationService;

	@Autowired
	private IStoreService storeService;

	@Autowired
	private ICategoryService categoryService;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private Validator validator;

	@GetMapping("/category/{categoryId}")
	public ResponseEntity<SuccessResponseDto> getCategory(HttpServletRequest request,
			@PathVariable(name = "categoryId") Long categoryId) {
		Store store = storeService
				.getStore(authenticationService.getAuthenticatedUser(authenticationService.getAuthentication()));

		Category category = categoryService.getCategory(categoryId, store);

		CategoryDto categoryDto = modelMapper.map(category, CategoryDto.class);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Category fetched successfully").data(categoryDto).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}

	@GetMapping("/category")
	public ResponseEntity<SuccessResponseDto> getCategories(HttpServletRequest request) {
		Store store = storeService
				.getStore(authenticationService.getAuthenticatedUser(authenticationService.getAuthentication()));

		List<Category> categories = categoryService.getCategories(store);

		List<CategoryDto> categoryDtos = categories.stream().map(e -> modelMapper.map(e, CategoryDto.class))
				.collect(Collectors.toList());

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Categories fetched successfully").data(categoryDtos).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}

	@PostMapping("/category")
	public ResponseEntity<SuccessResponseDto> createCategory(HttpServletRequest request,
			@RequestBody CategoryDto payload) {
		Set<ConstraintViolation<CategoryDto>> violations = validator.validate(payload);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException("Constraint violation", violations);
		}

		Store store = storeService
				.getStore(authenticationService.getAuthenticatedUser(authenticationService.getAuthentication()));

		Category category = categoryService.createCategory(store, payload);

		CategoryDto categoryDto = modelMapper.map(category, CategoryDto.class);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Category created successfully").data(categoryDto).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}

	@PutMapping("/category/{categoryId}")
	public ResponseEntity<SuccessResponseDto> updateCategory(HttpServletRequest request,
			@PathVariable(name = "categoryId") Long categoryId, @RequestBody CategoryDto payload) {
		Set<ConstraintViolation<CategoryDto>> violations = validator.validate(payload);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException("Constraint violation", violations);
		}

		Store store = storeService
				.getStore(authenticationService.getAuthenticatedUser(authenticationService.getAuthentication()));

		Category category = categoryService.updateCategory(categoryId, store, payload);

		CategoryDto categoryDto = modelMapper.map(category, CategoryDto.class);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Category updated successfully").data(categoryDto).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}

	@PatchMapping("/category/{categoryId}/status")
	public ResponseEntity<SuccessResponseDto> updateCategoryStatus(HttpServletRequest request,
			@PathVariable(name = "categoryId") Long categoryId, @RequestBody UpdateStatusDto payload) {
		Set<ConstraintViolation<UpdateStatusDto>> violations = validator.validate(payload);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException("Constraint violation", violations);
		}

		Store store = storeService
				.getStore(authenticationService.getAuthenticatedUser(authenticationService.getAuthentication()));

		Category category = categoryService.updateCategoryStatus(categoryId, store, payload.isEnabled());

		CategoryDto categoryDto = modelMapper.map(category, CategoryDto.class);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Category status updated successfully").data(categoryDto).path(request.getServletPath())
				.build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}

	@DeleteMapping("/category/{categoryId}")
	public ResponseEntity<SuccessResponseDto> deleteCategory(HttpServletRequest request,
			@PathVariable(name = "categoryId") Long categoryId) {

		Store store = storeService
				.getStore(authenticationService.getAuthenticatedUser(authenticationService.getAuthentication()));

		categoryService.deleteCategory(categoryId, store);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Category deleted successfully").data(null).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}
}
