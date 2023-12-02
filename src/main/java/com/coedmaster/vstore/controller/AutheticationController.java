package com.coedmaster.vstore.controller;

import java.time.LocalDateTime;
import java.util.Set;

import org.apache.commons.lang3.ObjectUtils;
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
import com.coedmaster.vstore.dto.AccessTokenDto;
import com.coedmaster.vstore.dto.ResetPasswordDto;
import com.coedmaster.vstore.dto.response.SuccessResponseDto;
import com.coedmaster.vstore.exception.InvalidMobileVerificationCodeException;
import com.coedmaster.vstore.exception.MobileVerificationCodeNotFoundException;
import com.coedmaster.vstore.service.MobileVerificationService;
import com.coedmaster.vstore.service.UserService;
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
	private UserService userService;

	@Autowired
	private MobileVerificationService mobileVerificationService;

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

		AccessTokenDto accessTokenDto = AccessTokenDto.builder().token(authAccessToken.getToken()).build();

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Authenticate successfully").data(accessTokenDto).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}

	@PostMapping("/resetpassword")
	public ResponseEntity<SuccessResponseDto> resetPassword(HttpServletRequest request,
			@RequestBody ResetPasswordDto payload) {
		Set<ConstraintViolation<ResetPasswordDto>> violations = validator.validate(payload);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException("Constraint violation", violations);
		}

		User user = userService.getUserByMobile(payload.getMobile());

		if (ObjectUtils.isEmpty(payload.getVerificationCode())) {
			mobileVerificationService.initVerification(payload.getMobile());

			throw new MobileVerificationCodeNotFoundException("Mobile verification code not found");
		}

		if (ObjectUtils.isNotEmpty(payload.getVerificationCode())
				&& !mobileVerificationService.verifyCode(payload.getMobile(), payload.getVerificationCode())) {
			throw new InvalidMobileVerificationCodeException("Invalid mobile verification code");
		}

		userService.updateUserPassword(user, payload.getPassword());

		authenticationService.deleteAllTokens(user);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Password reset successfully").data(null).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}
}
