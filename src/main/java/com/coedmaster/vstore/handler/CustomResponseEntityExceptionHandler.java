package com.coedmaster.vstore.handler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.coedmaster.vstore.dto.response.ErrorResponseDto;
import com.coedmaster.vstore.dto.response.ValidationErrorResponseDto;
import com.coedmaster.vstore.exception.EntityNotFoundException;
import com.coedmaster.vstore.exception.UsernameAlreadyTakenException;
import com.coedmaster.vstore.validation.Violation;

import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler({ UsernameAlreadyTakenException.class, EntityNotFoundException.class })
	public ResponseEntity<ErrorResponseDto> handleEntityNotFoundException(Exception ex, WebRequest request) {

		ErrorResponseDto errorResponseDto = ErrorResponseDto.builder().timestamp(LocalDateTime.now()).status(400)
				.error(HttpStatus.BAD_REQUEST).message(ex.getMessage()).path(request.getDescription(false)).build();

		return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
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
