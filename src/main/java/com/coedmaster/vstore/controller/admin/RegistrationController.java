package com.coedmaster.vstore.controller.admin;

import java.time.LocalDateTime;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coedmaster.vstore.dto.UserDto;
import com.coedmaster.vstore.dto.request.RegistrationRequestDto;
import com.coedmaster.vstore.dto.response.SuccessResponseDto;
import com.coedmaster.vstore.model.User;
import com.coedmaster.vstore.service.RegistrationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

@RestController("AdminRegistrationController")
@RequestMapping("/api/admin")
public class RegistrationController {

	@Autowired
	private RegistrationService registrationService;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private Validator validator;

	@PostMapping("/register")
	public ResponseEntity<SuccessResponseDto> register(HttpServletRequest request,
			@RequestBody RegistrationRequestDto payload) {
		Set<ConstraintViolation<RegistrationRequestDto>> violations = validator.validate(payload);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException("Constraint violation", violations);
		}

		User user = registrationService.registerAdmin(payload);

		UserDto userDto = modelMapper.map(user, UserDto.class);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Registration Successful").data(userDto).path("uri=" + request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}
}
