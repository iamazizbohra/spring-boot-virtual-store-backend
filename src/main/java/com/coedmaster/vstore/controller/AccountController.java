package com.coedmaster.vstore.controller;

import java.time.LocalDateTime;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.coedmaster.vstore.dto.CreateAccountDto;
import com.coedmaster.vstore.dto.AccessTokenDto;
import com.coedmaster.vstore.dto.UpdateAccountDto;
import com.coedmaster.vstore.dto.UpdatePasswordDto;
import com.coedmaster.vstore.dto.UserDto;
import com.coedmaster.vstore.dto.response.SuccessResponseDto;
import com.coedmaster.vstore.model.AuthAccessToken;
import com.coedmaster.vstore.model.User;
import com.coedmaster.vstore.service.contract.IAccountService;
import com.coedmaster.vstore.service.contract.IAuthenticationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

@RestController("AccountController")
public class AccountController {

	@Autowired
	private IAuthenticationService authenticationService;

	@Autowired
	private IAccountService accountService;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private Validator validator;

	@GetMapping("/account")
	public ResponseEntity<SuccessResponseDto> getAccount(HttpServletRequest request) {
		User user = authenticationService.getAuthenticatedUser(authenticationService.getAuthentication());

		UserDto userDto = modelMapper.map(user, UserDto.class);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Account fetched successfully").data(userDto).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}

	@PostMapping("/admin/account")
	public ResponseEntity<SuccessResponseDto> createAdminAccount(HttpServletRequest request,
			@RequestBody CreateAccountDto payload) {
		Set<ConstraintViolation<CreateAccountDto>> violations = validator.validate(payload);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException("Constraint violation", violations);
		}

		User user = accountService.createAdminAccount(payload);

		UserDto userDto = modelMapper.map(user, UserDto.class);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Account created successfully").data(userDto).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}

	@PostMapping("/buyer/account")
	public ResponseEntity<SuccessResponseDto> createBuyerAccount(HttpServletRequest request,
			@RequestBody CreateAccountDto payload) {
		Set<ConstraintViolation<CreateAccountDto>> violations = validator.validate(payload);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException("Constraint violation", violations);
		}

		User user = accountService.createBuyerAccount(payload);

		UserDto userDto = modelMapper.map(user, UserDto.class);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Account created successfully").data(userDto).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}

	@PostMapping("/seller/account")
	public ResponseEntity<SuccessResponseDto> createSellerAccount(HttpServletRequest request,
			@RequestBody CreateAccountDto payload) {
		Set<ConstraintViolation<CreateAccountDto>> violations = validator.validate(payload);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException("Constraint violation", violations);
		}

		User user = accountService.createSellerAccount(payload);

		UserDto userDto = modelMapper.map(user, UserDto.class);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Account created successfully").data(userDto).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}

	@PutMapping("/account")
	public ResponseEntity<SuccessResponseDto> updateAccount(HttpServletRequest request,
			@RequestBody UpdateAccountDto payload) {
		Set<ConstraintViolation<UpdateAccountDto>> violations = validator.validate(payload);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException("Constraint violation", violations);
		}

		User user = authenticationService.getAuthenticatedUser(authenticationService.getAuthentication());

		user = accountService.updateAccount(user, payload);

		UserDto userDto = modelMapper.map(user, UserDto.class);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Account updated successfully").data(userDto).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}

	@PatchMapping("/account/password")
	public ResponseEntity<SuccessResponseDto> updatePassword(HttpServletRequest request,
			@RequestBody UpdatePasswordDto payload) {
		Set<ConstraintViolation<UpdatePasswordDto>> violations = validator.validate(payload);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException("Constraint violation", violations);
		}

		User user = authenticationService.getAuthenticatedUser(authenticationService.getAuthentication());

		AuthAccessToken authAccessToken = accountService.updatePassword(user, payload);

		AccessTokenDto accessTokenDto = AccessTokenDto.builder().token(authAccessToken.getToken()).build();

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Password updated successfully").data(accessTokenDto).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}
}
