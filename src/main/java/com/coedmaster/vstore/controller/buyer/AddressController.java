package com.coedmaster.vstore.controller.buyer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coedmaster.vstore.dto.AddressDto;
import com.coedmaster.vstore.dto.response.SuccessResponseDto;
import com.coedmaster.vstore.model.Address;
import com.coedmaster.vstore.model.User;
import com.coedmaster.vstore.service.AddressService;
import com.coedmaster.vstore.service.AuthenticationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

@RestController
@RequestMapping("/buyer")
public class AddressController {
	@Autowired
	private AddressService addressService;

	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private Validator validator;

	@GetMapping("/address/{id}")
	public ResponseEntity<SuccessResponseDto> getAddress(HttpServletRequest request,
			@PathVariable(name = "id") Long id) {
		User user = authenticationService.getAuthenticatedUser(authenticationService.getAuthentication());

		Address address = addressService.getAddress(id, user);

		AddressDto addressDto = modelMapper.map(address, AddressDto.class);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Address fetched successfully").data(addressDto).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}

	@GetMapping("/address")
	public ResponseEntity<SuccessResponseDto> getAddresses(HttpServletRequest request) {
		User user = authenticationService.getAuthenticatedUser(authenticationService.getAuthentication());

		List<Address> addresses = addressService.getAddresses(user);

		List<AddressDto> addressDto = addresses.stream().map(e -> modelMapper.map(e, AddressDto.class))
				.collect(Collectors.toList());

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Addresses fetched successfully").data(addressDto).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}

	@PostMapping("/address")
	public ResponseEntity<SuccessResponseDto> createAddress(HttpServletRequest request,
			@RequestBody AddressDto payload) {
		Set<ConstraintViolation<AddressDto>> violations = validator.validate(payload);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException("Constraint violation", violations);
		}

		User user = authenticationService.getAuthenticatedUser(authenticationService.getAuthentication());

		Address address = addressService.createAddress(user, payload);

		AddressDto addressDto = modelMapper.map(address, AddressDto.class);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Address created successfully").data(addressDto).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}

	@PutMapping("/address/{id}")
	public ResponseEntity<SuccessResponseDto> updateAddress(HttpServletRequest request,
			@PathVariable(name = "id") Long id, @RequestBody AddressDto payload) {
		Set<ConstraintViolation<AddressDto>> violations = validator.validate(payload);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException("Constraint violation", violations);
		}

		User user = authenticationService.getAuthenticatedUser(authenticationService.getAuthentication());

		Address address = addressService.updateAddress(id, user, payload);

		AddressDto addressDto = modelMapper.map(address, AddressDto.class);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Address updated successfully").data(addressDto).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}

	@PatchMapping("/address/{id}/default")
	public ResponseEntity<SuccessResponseDto> setDefaultAddress(HttpServletRequest request,
			@PathVariable(name = "id") Long id) {
		User user = authenticationService.getAuthenticatedUser(authenticationService.getAuthentication());

		Address address = addressService.setDefaultAddress(id, user);

		AddressDto addressDto = modelMapper.map(address, AddressDto.class);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Address updated successfully").data(addressDto).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}

	@DeleteMapping("/address/{id}")
	public ResponseEntity<SuccessResponseDto> deleteAddress(HttpServletRequest request,
			@PathVariable(name = "id") Long id) {
		User user = authenticationService.getAuthenticatedUser(authenticationService.getAuthentication());

		addressService.deleteAddress(id, user);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Address deleted successfully").data(null).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}
}
