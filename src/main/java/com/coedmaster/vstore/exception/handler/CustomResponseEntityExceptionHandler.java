package com.coedmaster.vstore.exception.handler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.coedmaster.vstore.dto.response.ErrorResponseDto;
import com.coedmaster.vstore.dto.response.ValidationErrorResponseDto;
import com.coedmaster.vstore.exception.EntityAlreadyExistsException;
import com.coedmaster.vstore.exception.EntityNotFoundException;
import com.coedmaster.vstore.exception.InvalidMobileVerificationCodeException;
import com.coedmaster.vstore.exception.MobileVerificationCodeNotFoundException;
import com.coedmaster.vstore.exception.StoreCodeAlreadyTakenException;
import com.coedmaster.vstore.exception.UnallowedOperationException;
import com.coedmaster.vstore.exception.UsernameAlreadyTakenException;
import com.coedmaster.vstore.validation.Violation;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ErrorResponseDto> handleAllUncaughtException(Exception ex, WebRequest request) {

		ErrorResponseDto errorResponseDto = ErrorResponseDto.builder().timestamp(LocalDateTime.now()).status(500)
				.error(HttpStatus.INTERNAL_SERVER_ERROR).stackTrace(Arrays.toString(ex.getStackTrace()))
				.message(ex.getMessage()).path(request.getDescription(false)).build();

		return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler({ EntityNotFoundException.class, EntityAlreadyExistsException.class,
			UsernameAlreadyTakenException.class, StoreCodeAlreadyTakenException.class,
			UnallowedOperationException.class, MobileVerificationCodeNotFoundException.class,
			InvalidMobileVerificationCodeException.class })
	public ResponseEntity<ErrorResponseDto> handleAllCustomException(Exception ex, WebRequest request) {

		ErrorResponseDto errorResponseDto = ErrorResponseDto.builder().timestamp(LocalDateTime.now()).status(400)
				.error(HttpStatus.BAD_REQUEST).message(ex.getMessage()).path(request.getDescription(false)).build();

		return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ErrorResponseDto> handleAuthenticationException(Exception ex, WebRequest request) {

		ErrorResponseDto errorResponseDto = ErrorResponseDto.builder().timestamp(LocalDateTime.now()).status(401)
				.error(HttpStatus.UNAUTHORIZED).message(ex.getMessage()).path(request.getDescription(false)).build();

		return new ResponseEntity<>(errorResponseDto, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorResponseDto> handleAccessDeniedExceptionException(Exception ex, WebRequest request) {

		ErrorResponseDto errorResponseDto = ErrorResponseDto.builder().timestamp(LocalDateTime.now()).status(403)
				.error(HttpStatus.FORBIDDEN).message(ex.getMessage()).path(request.getDescription(false)).build();

		return new ResponseEntity<>(errorResponseDto, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	ResponseEntity<ValidationErrorResponseDto> handleConstraintViolationException(ConstraintViolationException ex,
			WebRequest request) {
		List<Violation> violations = new ArrayList<>();

		ex.getConstraintViolations().stream().forEach((e) -> violations
				.add(Violation.builder().fieldName(e.getPropertyPath().toString()).message(e.getMessage()).build()));

		ValidationErrorResponseDto validationErrorResponseDto = ValidationErrorResponseDto.builder()
				.timestamp(LocalDateTime.now()).status(400).error(HttpStatus.BAD_REQUEST).message(ex.getMessage())
				.violations(violations).path(request.getDescription(false)).build();

		return new ResponseEntity<>(validationErrorResponseDto, HttpStatus.BAD_REQUEST);
	}
}
