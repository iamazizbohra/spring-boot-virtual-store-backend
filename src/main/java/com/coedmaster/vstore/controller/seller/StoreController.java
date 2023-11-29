package com.coedmaster.vstore.controller.seller;

import java.time.LocalDateTime;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coedmaster.vstore.domain.Store;
import com.coedmaster.vstore.domain.User;
import com.coedmaster.vstore.dto.StoreDto;
import com.coedmaster.vstore.dto.response.SuccessResponseDto;
import com.coedmaster.vstore.service.contract.IAuthenticationService;
import com.coedmaster.vstore.service.contract.IStoreService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

@RestController("SellerStoreController")
@RequestMapping("/seller")
public class StoreController {

	@Autowired
	private IAuthenticationService authenticationService;

	@Autowired
	private IStoreService storeService;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private Validator validator;

	@GetMapping("/store")
	public ResponseEntity<SuccessResponseDto> getStore(HttpServletRequest request) {
		User user = authenticationService.getAuthenticatedUser(authenticationService.getAuthentication());

		Store store = storeService.getStoreByUser(user);

		StoreDto storeDto = modelMapper.map(store, StoreDto.class);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Store fetched successfully").data(storeDto).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}

	@PostMapping("/store")
	public ResponseEntity<SuccessResponseDto> createStore(HttpServletRequest request, @RequestBody StoreDto payload) {
		Set<ConstraintViolation<StoreDto>> violations = validator.validate(payload);
		if (!violations.isEmpty())
			throw new ConstraintViolationException("Constraint violation", violations);

		User user = authenticationService.getAuthenticatedUser(authenticationService.getAuthentication());

		Store store = storeService.createStore(user, payload);

		StoreDto storeDto = modelMapper.map(store, StoreDto.class);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Store created successfully").data(storeDto).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}

	@PutMapping("/store")
	public ResponseEntity<SuccessResponseDto> updateStore(HttpServletRequest request, @RequestBody StoreDto payload) {
		Set<ConstraintViolation<StoreDto>> violations = validator.validate(payload);
		if (!violations.isEmpty())
			throw new ConstraintViolationException("Constraint violation", violations);

		User user = authenticationService.getAuthenticatedUser(authenticationService.getAuthentication());

		Store store = storeService.getStoreByUser(user);

		store = storeService.updateStore(store, payload);

		StoreDto storeDto = modelMapper.map(store, StoreDto.class);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Store updated successfully").data(storeDto).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}
}
