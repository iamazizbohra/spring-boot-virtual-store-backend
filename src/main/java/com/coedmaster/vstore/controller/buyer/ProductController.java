package com.coedmaster.vstore.controller.buyer;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.coedmaster.vstore.dto.ProductDto;
import com.coedmaster.vstore.dto.response.SuccessResponseDto;
import com.coedmaster.vstore.model.Category;
import com.coedmaster.vstore.model.Product;
import com.coedmaster.vstore.model.Store;
import com.coedmaster.vstore.service.contract.ICategoryService;
import com.coedmaster.vstore.service.contract.IProductService;
import com.coedmaster.vstore.service.contract.IStoreService;

import jakarta.servlet.http.HttpServletRequest;

@RestController("BuyerProductController")
@RequestMapping("/buyer")
public class ProductController {
	
	@Autowired
	private IProductService productService;

	@Autowired
	private ICategoryService categoryService;

	@Autowired
	private IStoreService storeService;

	@Autowired
	private ModelMapper modelMapper;

	@GetMapping("/store/{storeId}/product/{productId}")
	public ResponseEntity<SuccessResponseDto> getProduct(HttpServletRequest request,
			@PathVariable(name = "storeId") Long storeId, @PathVariable(name = "productId") Long productId) {
		Store store = storeService.getStoreById(storeId);

		Product product = productService.getProduct(productId, store);

		ProductDto productDto = modelMapper.map(product, ProductDto.class);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Product fetched successfully").data(productDto).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}

	@GetMapping("/store/{storeId}/product")
	public ResponseEntity<SuccessResponseDto> getProducts(HttpServletRequest request,
			@PathVariable(name = "storeId") Long storeId,
			@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
			@RequestParam(value = "categoryIds", defaultValue = "") List<Long> categoryIds,
			@RequestParam(value = "sortBy", defaultValue = "lastModifiedDate") String sortBy,
			@RequestParam(value = "sortDirection", defaultValue = "DESC") String sortDirection) {
		Store store = storeService.getStoreById(storeId);

		PageRequest paging = PageRequest.of(pageNumber, pageSize,
				Sort.by(Sort.Direction.valueOf(sortDirection), sortBy));

		Page<Product> productsPage;
		if (categoryIds.size() == 0) {
			productsPage = productService.getProducts(store, paging);
		} else {
			List<Category> categories = categoryService.getCategories(categoryIds, store);
			productsPage = productService.getProducts(store, categories, paging);
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
}
