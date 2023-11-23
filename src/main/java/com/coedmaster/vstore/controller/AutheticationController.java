package com.coedmaster.vstore.controller;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.coedmaster.vstore.dto.AuthenticationDto;
import com.coedmaster.vstore.dto.JwtTokenDto;
import com.coedmaster.vstore.dto.response.SuccessResponseDto;
import com.coedmaster.vstore.service.IAuthenticationService;

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
	public ResponseEntity<SuccessResponseDto> authenticate(HttpServletRequest request, @RequestBody AuthenticationDto payload) {
		Set<ConstraintViolation<AuthenticationDto>> violations = validator.validate(payload);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException("Constraint violation", violations);
		}

		String jws = authenticationService.authenticate(payload);

		JwtTokenDto jwtTokenDto = JwtTokenDto.builder().accessToken(jws).build();

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Authenticate successful").data(jwtTokenDto).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}
}
