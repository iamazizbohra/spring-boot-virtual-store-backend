package com.coedmaster.vstore.controller.seller;

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

import com.coedmaster.vstore.dto.UpdateSortOrderDto;
import com.coedmaster.vstore.dto.UpdateStatusDto;
import com.coedmaster.vstore.dto.request.BannerRequestDto;
import com.coedmaster.vstore.dto.response.BannerResponseDto;
import com.coedmaster.vstore.dto.response.SuccessResponseDto;
import com.coedmaster.vstore.model.Banner;
import com.coedmaster.vstore.model.Store;
import com.coedmaster.vstore.service.AuthenticationService;
import com.coedmaster.vstore.service.BannerService;
import com.coedmaster.vstore.service.IStoreService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

@RestController
@RequestMapping("/seller")
public class BannerController {

	@Autowired
	private BannerService bannerService;

	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private IStoreService storeService;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private Validator validator;

	@GetMapping("/banner/{id}")
	public ResponseEntity<SuccessResponseDto> getBanner(HttpServletRequest request,
			@PathVariable(value = "id") Long id) {
		Store store = storeService
				.getStoreByUser(authenticationService.getAuthenticatedUser(authenticationService.getAuthentication()));

		Banner banner = bannerService.getBanner(id, store);

		BannerResponseDto bannerResponseDto = modelMapper.map(banner, BannerResponseDto.class);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Banner fetched successfully").data(bannerResponseDto).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}

	@GetMapping("/banner")
	public ResponseEntity<SuccessResponseDto> getBanners(HttpServletRequest request) {
		Store store = storeService
				.getStoreByUser(authenticationService.getAuthenticatedUser(authenticationService.getAuthentication()));

		List<Banner> banners = bannerService.getBanners(store);

		List<BannerResponseDto> bannerResponseDto = banners.stream()
				.map(e -> modelMapper.map(e, BannerResponseDto.class)).collect(Collectors.toList());

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Banners fetched successfully").data(bannerResponseDto).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}

	@PostMapping("/banner")
	public ResponseEntity<SuccessResponseDto> createBanner(HttpServletRequest request,
			@RequestBody BannerRequestDto payload) {
		Set<ConstraintViolation<BannerRequestDto>> violations = validator.validate(payload);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException("Constraint violation", violations);
		}

		Store store = storeService
				.getStoreByUser(authenticationService.getAuthenticatedUser(authenticationService.getAuthentication()));

		Banner banner = bannerService.createBanner(store, payload);

		BannerResponseDto bannerResponseDto = modelMapper.map(banner, BannerResponseDto.class);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Banner created successfully").data(bannerResponseDto).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);

	}

	@PutMapping("/banner/{id}")
	public ResponseEntity<SuccessResponseDto> updateBanner(HttpServletRequest request,
			@PathVariable(value = "id") Long id, @RequestBody BannerRequestDto payload) {
		Set<ConstraintViolation<BannerRequestDto>> violations = validator.validate(payload);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException("Constraint violation", violations);
		}

		Store store = storeService
				.getStoreByUser(authenticationService.getAuthenticatedUser(authenticationService.getAuthentication()));

		Banner banner = bannerService.updateBanner(id, store, payload);

		BannerResponseDto bannerResponseDto = modelMapper.map(banner, BannerResponseDto.class);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Banner updated successfully").data(bannerResponseDto).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}

	@DeleteMapping("/banner/{id}")
	public ResponseEntity<SuccessResponseDto> deleteBanner(HttpServletRequest request,
			@PathVariable(value = "id") Long id) {
		Store store = storeService
				.getStoreByUser(authenticationService.getAuthenticatedUser(authenticationService.getAuthentication()));

		bannerService.deleteBanner(id, store);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Banner deleted successfully").data(null).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}

	@PatchMapping("/banner/{id}/status")
	public ResponseEntity<SuccessResponseDto> updateBannerStatus(HttpServletRequest request,
			@PathVariable(value = "id") Long id, @RequestBody UpdateStatusDto payload) {
		Set<ConstraintViolation<UpdateStatusDto>> violations = validator.validate(payload);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException("Constraint violation", violations);
		}

		Store store = storeService
				.getStoreByUser(authenticationService.getAuthenticatedUser(authenticationService.getAuthentication()));

		Banner banner = bannerService.updateBannerStatus(id, store, payload);

		BannerResponseDto bannerResponseDto = modelMapper.map(banner, BannerResponseDto.class);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Banner status updated successfully").data(bannerResponseDto).path(request.getServletPath())
				.build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}

	@PatchMapping("/banner/{id}/sortorder")
	public ResponseEntity<SuccessResponseDto> updateBannerSortOrder(HttpServletRequest request,
			@PathVariable(value = "id") Long id, @RequestBody UpdateSortOrderDto payload) {
		Set<ConstraintViolation<UpdateSortOrderDto>> violations = validator.validate(payload);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException("Constraint violation", violations);
		}

		Store store = storeService
				.getStoreByUser(authenticationService.getAuthenticatedUser(authenticationService.getAuthentication()));

		Banner banner = bannerService.updateBannerSortOrder(id, store, payload);

		BannerResponseDto bannerResponseDto = modelMapper.map(banner, BannerResponseDto.class);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Banner sort order updated successfully").data(bannerResponseDto).path(request.getServletPath())
				.build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}

}
