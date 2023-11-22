package com.coedmaster.vstore.controller.buyer;

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

import com.coedmaster.vstore.dto.AccountDto;
import com.coedmaster.vstore.dto.UserDto;
import com.coedmaster.vstore.dto.response.SuccessResponseDto;
import com.coedmaster.vstore.model.User;
import com.coedmaster.vstore.service.AccountService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

@RestController("BuyerAccountController")
@RequestMapping("/api/buyer")
public class AccountController {
	@Autowired
	private AccountService accountService;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private Validator validator;

	@PostMapping("/account")
	public ResponseEntity<SuccessResponseDto> register(HttpServletRequest request, @RequestBody AccountDto payload) {
		Set<ConstraintViolation<AccountDto>> violations = validator.validate(payload);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException("Constraint violation", violations);
		}

		User user = accountService.createBuyerAccount(payload);

		UserDto userDto = modelMapper.map(user, UserDto.class);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Registration Successful").data(userDto).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}
}
