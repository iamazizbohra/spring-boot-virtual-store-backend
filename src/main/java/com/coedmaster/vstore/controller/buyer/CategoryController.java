package com.coedmaster.vstore.controller.buyer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coedmaster.vstore.dto.response.CategoryResponseDto;
import com.coedmaster.vstore.dto.response.SuccessResponseDto;
import com.coedmaster.vstore.model.Category;
import com.coedmaster.vstore.model.Store;
import com.coedmaster.vstore.service.CategoryService;
import com.coedmaster.vstore.service.IStoreService;

import jakarta.servlet.http.HttpServletRequest;

@RestController("BuyerCategoryController")
@RequestMapping("/buyer")
public class CategoryController {
	@Autowired
	private CategoryService categoryService;

	@Autowired
	private IStoreService storeService;

	@Autowired
	private ModelMapper modelMapper;

	@GetMapping("/store/{storeId}/category")
	public ResponseEntity<SuccessResponseDto> getCategories(HttpServletRequest request,
			@PathVariable(name = "storeId") Long storeId) {
		Store store = storeService.getStoreById(storeId);

		List<Category> categories = categoryService.getCategories(store);

		List<CategoryResponseDto> categoryResponseDtos = categories.stream()
				.map(e -> modelMapper.map(e, CategoryResponseDto.class)).collect(Collectors.toList());

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Categories fetched successfully").data(categoryResponseDtos).path(request.getServletPath())
				.build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}
}