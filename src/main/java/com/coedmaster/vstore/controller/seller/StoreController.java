package com.coedmaster.vstore.controller.seller;

import java.time.LocalDateTime;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coedmaster.vstore.dto.StoreDto;
import com.coedmaster.vstore.dto.response.SuccessResponseDto;
import com.coedmaster.vstore.model.Store;
import com.coedmaster.vstore.service.IStoreService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

@RestController
@RequestMapping("/api/seller")
public class StoreController {
	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private Validator validator;

	@Autowired
	private IStoreService storeService;

	@GetMapping("/store/{code}")
	public ResponseEntity<SuccessResponseDto> getStore(HttpServletRequest request,
			@PathVariable(value = "code") String code) {

		Store store = storeService.getStoreByCode(code);

		StoreDto storeDto = modelMapper.map(store, StoreDto.class);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Store fetched successful").data(storeDto).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}

	@PostMapping("/store")
	public ResponseEntity<SuccessResponseDto> createStore(HttpServletRequest request, @RequestBody StoreDto payload) {
		Set<ConstraintViolation<StoreDto>> violations = validator.validate(payload);
		if (!violations.isEmpty())
			throw new ConstraintViolationException("Constraint violation", violations);

		Store store = storeService.createStore(payload);

		StoreDto storeDto = modelMapper.map(store, StoreDto.class);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Store created successful").data(storeDto).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}

	@PutMapping("/store/{id}")
	public ResponseEntity<SuccessResponseDto> updateStore(HttpServletRequest request,
			@PathVariable(value = "id") Long id, @RequestBody StoreDto payload) {
		Set<ConstraintViolation<StoreDto>> violations = validator.validate(payload);
		if (!violations.isEmpty())
			throw new ConstraintViolationException("Constraint violation", violations);

		Store store = storeService.updateStore(id, payload);

		StoreDto storeDto = modelMapper.map(store, StoreDto.class);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Store created successful").data(storeDto).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}
}
