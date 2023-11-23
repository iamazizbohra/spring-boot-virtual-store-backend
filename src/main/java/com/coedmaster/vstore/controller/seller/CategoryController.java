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
import com.coedmaster.vstore.service.CategoryService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

@RestController
@RequestMapping("/seller")
public class CategoryController {
	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private Validator validator;

	@Autowired
	private CategoryService categoryService;

	@GetMapping("/category")
	public ResponseEntity<SuccessResponseDto> getCategories(HttpServletRequest request) {
		List<Category> categories = categoryService.getCategories();

		List<CategoryResponseDto> categoryResponseDtos = categories.stream()
				.map(e -> modelMapper.map(e, CategoryResponseDto.class)).collect(Collectors.toList());

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Categories fetched successful").data(categoryResponseDtos).path(request.getServletPath())
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

		Category category = categoryService.createCategory(payload);

		CategoryResponseDto categoryResponseDto = modelMapper.map(category, CategoryResponseDto.class);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Category created successful").data(categoryResponseDto).path(request.getServletPath())
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

		Category category = categoryService.updateCategory(id, payload);

		CategoryResponseDto categoryResponseDto = modelMapper.map(category, CategoryResponseDto.class);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Category updated successful").data(categoryResponseDto).path(request.getServletPath())
				.build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}

	@DeleteMapping("/category/{id}")
	public ResponseEntity<SuccessResponseDto> deleteCategory(HttpServletRequest request,
			@PathVariable(value = "id") Long id) {

		categoryService.deleteCategory(id);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Category deleted successful").data(null).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}

	@PatchMapping("/category/{id}/status")
	public ResponseEntity<SuccessResponseDto> updateCategoryStatus(HttpServletRequest request,
			@PathVariable(value = "id") Long id, @RequestBody UpdateStatusDto payload) {
		Set<ConstraintViolation<UpdateStatusDto>> violations = validator.validate(payload);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException("Constraint violation", violations);
		}

		Category category = categoryService.updateCategoryStatus(id, payload);

		CategoryResponseDto categoryResponseDto = modelMapper.map(category, CategoryResponseDto.class);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Category status updated successful").data(categoryResponseDto).path(request.getServletPath())
				.build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}

}
