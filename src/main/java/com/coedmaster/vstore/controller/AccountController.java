package com.coedmaster.vstore.controller;

import java.time.LocalDateTime;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coedmaster.vstore.dto.JwtTokenDto;
import com.coedmaster.vstore.dto.UpdatePasswordDto;
import com.coedmaster.vstore.dto.UserDto;
import com.coedmaster.vstore.dto.request.AccountRequestDto;
import com.coedmaster.vstore.dto.response.AccountResponseDto;
import com.coedmaster.vstore.dto.response.SuccessResponseDto;
import com.coedmaster.vstore.model.User;
import com.coedmaster.vstore.model.UserDetailsImpl;
import com.coedmaster.vstore.service.AccountService;
import com.coedmaster.vstore.service.AuthenticationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

@RestController("AccountController")
@RequestMapping("/api")
public class AccountController {
	@Autowired
	private AccountService accountService;

	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private Validator validator;

	@PostMapping("/admin/account")
	public ResponseEntity<SuccessResponseDto> createAdminAccount(HttpServletRequest request,
			@RequestBody AccountRequestDto payload) {
		Set<ConstraintViolation<AccountRequestDto>> violations = validator.validate(payload);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException("Constraint violation", violations);
		}

		User user = accountService.createAdminAccount(payload);

		UserDto userDto = modelMapper.map(user, UserDto.class);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Account Created Successful").data(userDto).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}

	@PostMapping("/buyer/account")
	public ResponseEntity<SuccessResponseDto> createBuyerAccount(HttpServletRequest request,
			@RequestBody AccountRequestDto payload) {
		Set<ConstraintViolation<AccountRequestDto>> violations = validator.validate(payload);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException("Constraint violation", violations);
		}

		User user = accountService.createBuyerAccount(payload);

		UserDto userDto = modelMapper.map(user, UserDto.class);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Account Created Successful").data(userDto).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}

	@PostMapping("/seller/account")
	public ResponseEntity<SuccessResponseDto> createSellerAccount(HttpServletRequest request,
			@RequestBody AccountRequestDto payload) {
		Set<ConstraintViolation<AccountRequestDto>> violations = validator.validate(payload);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException("Constraint violation", violations);
		}

		User user = accountService.createSellerAccount(payload);

		UserDto userDto = modelMapper.map(user, UserDto.class);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Account Created Successful").data(userDto).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}

	@PutMapping("/account")
	public ResponseEntity<SuccessResponseDto> updateAccount(HttpServletRequest request,
			@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody AccountRequestDto payload) {
		Set<ConstraintViolation<AccountRequestDto>> violations = validator.validate(payload);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException("Constraint violation", violations);
		}

		String jws = null;
		final String oldMobile = userDetails.getMobile();

		User user = accountService.updateAccount(payload);
		UserDto userDto = modelMapper.map(user, UserDto.class);

		// issue new token if mobile number is changed
		if (!oldMobile.equals(user.getMobile())) {
			jws = authenticationService.generateToken();
		}
		JwtTokenDto jwtTokenDto = JwtTokenDto.builder().accessToken(jws).build();

		AccountResponseDto accountResponseDto = AccountResponseDto.builder().user(userDto).jwt(jwtTokenDto).build();

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Account Updated Successful").data(accountResponseDto).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}

	@PatchMapping("/account/password")
	public ResponseEntity<SuccessResponseDto> updatePassword(HttpServletRequest request,
			@RequestBody UpdatePasswordDto payload) {
		Set<ConstraintViolation<UpdatePasswordDto>> violations = validator.validate(payload);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException("Constraint violation", violations);
		}

		User user = accountService.updatePassword(payload);

		UserDto userDto = modelMapper.map(user, UserDto.class);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Password Updated Successful").data(userDto).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}
}
