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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.coedmaster.vstore.dto.StoreDto;
import com.coedmaster.vstore.dto.response.SuccessResponseDto;
import com.coedmaster.vstore.model.Store;
import com.coedmaster.vstore.service.IStoreService;

import jakarta.servlet.http.HttpServletRequest;

@RestController("BuyerStoreController")
@RequestMapping("/buyer")
public class StoreController {
	@Autowired
	private IStoreService storeService;

	@Autowired
	private ModelMapper modelMapper;

	@GetMapping("/store/{storeId}")
	public ResponseEntity<SuccessResponseDto> getStoreById(HttpServletRequest request,
			@PathVariable(name = "storeId") Long storeId) {
		Store store = storeService.getStoreById(storeId);

		StoreDto storeDto = modelMapper.map(store, StoreDto.class);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Store fetched successfully").data(storeDto).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}

	@GetMapping("/store/code/{code}")
	public ResponseEntity<SuccessResponseDto> getStoreByCode(HttpServletRequest request,
			@PathVariable(name = "code") String code) {
		Store store = storeService.getStoreByCode(code);

		StoreDto storeDto = modelMapper.map(store, StoreDto.class);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Store fetched successfully").data(storeDto).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}

	@GetMapping("/store")
	public ResponseEntity<SuccessResponseDto> getStores(HttpServletRequest request,
			@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("name"));

		Page<Store> storePage = storeService.getStores(pageable);

		List<StoreDto> storeDto = storePage.getContent().stream().map(e -> modelMapper.map(e, StoreDto.class))
				.collect(Collectors.toList());

		Map<String, Object> pageDetails = new HashMap<>();
		pageDetails.put("products", storeDto);
		pageDetails.put("currentPage", storePage.getNumber());
		pageDetails.put("totalItems", storePage.getTotalElements());
		pageDetails.put("totalPages", storePage.getTotalPages());

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Stores fetched successfully").data(pageDetails).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}
}
