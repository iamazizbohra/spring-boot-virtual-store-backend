package com.coedmaster.vstore.controller;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.coedmaster.vstore.domain.AuthAccessToken;
import com.coedmaster.vstore.domain.User;
import com.coedmaster.vstore.dto.AuthenticateDto;
import com.coedmaster.vstore.dto.JwtTokenDto;
import com.coedmaster.vstore.dto.response.SuccessResponseDto;
import com.coedmaster.vstore.service.contract.IAuthenticationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

@RestController
public class AutheticationController {

	@Autowired
	private IAuthenticationService authenticationService;

	@Autowired
	private Validator validator;

	@PostMapping("/authenticate")
	public ResponseEntity<SuccessResponseDto> authenticate(HttpServletRequest request,
			@RequestBody AuthenticateDto payload) {
		Set<ConstraintViolation<AuthenticateDto>> violations = validator.validate(payload);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException("Constraint violation", violations);
		}

		Authentication authentication = authenticationService.authenticate(payload.getUsername(),
				payload.getPassword());

		User user = authenticationService.getAuthenticatedUser(authentication);

		AuthAccessToken authAccessToken = authenticationService.generateToken(user);

		JwtTokenDto jwtTokenDto = JwtTokenDto.builder().accessToken(authAccessToken.getToken()).build();

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Authenticate successfully").data(jwtTokenDto).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}
}
