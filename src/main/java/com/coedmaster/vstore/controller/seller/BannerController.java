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

import com.coedmaster.vstore.domain.Banner;
import com.coedmaster.vstore.domain.Store;
import com.coedmaster.vstore.dto.BannerDto;
import com.coedmaster.vstore.dto.UpdateSortOrderDto;
import com.coedmaster.vstore.dto.UpdateStatusDto;
import com.coedmaster.vstore.dto.response.SuccessResponseDto;
import com.coedmaster.vstore.service.contract.IAuthenticationService;
import com.coedmaster.vstore.service.contract.IBannerService;
import com.coedmaster.vstore.service.contract.IStoreService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

@RestController
@RequestMapping("/seller")
public class BannerController {

	@Autowired
	private IAuthenticationService authenticationService;

	@Autowired
	private IBannerService bannerService;

	@Autowired
	private IStoreService storeService;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private Validator validator;

	@GetMapping("/banner/{bannerId}")
	public ResponseEntity<SuccessResponseDto> getBanner(HttpServletRequest request,
			@PathVariable(name = "bannerId") Long bannerId) {
		Store store = storeService
				.getStore(authenticationService.getAuthenticatedUser(authenticationService.getAuthentication()));

		Banner banner = bannerService.getBanner(bannerId, store);

		BannerDto bannerDto = modelMapper.map(banner, BannerDto.class);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Banner fetched successfully").data(bannerDto).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}

	@GetMapping("/banner")
	public ResponseEntity<SuccessResponseDto> getBanners(HttpServletRequest request) {
		Store store = storeService
				.getStore(authenticationService.getAuthenticatedUser(authenticationService.getAuthentication()));

		List<Banner> banners = bannerService.getBanners(store);

		List<BannerDto> bannerDtos = banners.stream().map(e -> modelMapper.map(e, BannerDto.class))
				.collect(Collectors.toList());

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Banners fetched successfully").data(bannerDtos).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}

	@PostMapping("/banner")
	public ResponseEntity<SuccessResponseDto> createBanner(HttpServletRequest request, @RequestBody BannerDto payload) {
		Set<ConstraintViolation<BannerDto>> violations = validator.validate(payload);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException("Constraint violation", violations);
		}

		Store store = storeService
				.getStore(authenticationService.getAuthenticatedUser(authenticationService.getAuthentication()));

		Banner banner = bannerService.createBanner(store, payload);

		BannerDto bannerDto = modelMapper.map(banner, BannerDto.class);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Banner created successfully").data(bannerDto).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);

	}

	@PutMapping("/banner/{bannerId}")
	public ResponseEntity<SuccessResponseDto> updateBanner(HttpServletRequest request,
			@PathVariable(name = "bannerId") Long bannerId, @RequestBody BannerDto payload) {
		Set<ConstraintViolation<BannerDto>> violations = validator.validate(payload);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException("Constraint violation", violations);
		}

		Store store = storeService
				.getStore(authenticationService.getAuthenticatedUser(authenticationService.getAuthentication()));

		Banner banner = bannerService.updateBanner(bannerId, store, payload);

		BannerDto bannerDto = modelMapper.map(banner, BannerDto.class);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Banner updated successfully").data(bannerDto).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}

	@PatchMapping("/banner/{bannerId}/status")
	public ResponseEntity<SuccessResponseDto> updateBannerStatus(HttpServletRequest request,
			@PathVariable(name = "bannerId") Long bannerId, @RequestBody UpdateStatusDto payload) {
		Set<ConstraintViolation<UpdateStatusDto>> violations = validator.validate(payload);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException("Constraint violation", violations);
		}

		Store store = storeService
				.getStore(authenticationService.getAuthenticatedUser(authenticationService.getAuthentication()));

		Banner banner = bannerService.updateBannerStatus(bannerId, store, payload.isEnabled());

		BannerDto bannerDto = modelMapper.map(banner, BannerDto.class);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Banner status updated successfully").data(bannerDto).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}

	@PatchMapping("/banner/{bannerId}/sortorder")
	public ResponseEntity<SuccessResponseDto> updateBannerSortOrder(HttpServletRequest request,
			@PathVariable(name = "bannerId") Long bannerId, @RequestBody UpdateSortOrderDto payload) {
		Set<ConstraintViolation<UpdateSortOrderDto>> violations = validator.validate(payload);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException("Constraint violation", violations);
		}

		Store store = storeService
				.getStore(authenticationService.getAuthenticatedUser(authenticationService.getAuthentication()));

		Banner banner = bannerService.updateBannerSortOrder(bannerId, store, payload.getSortOrder());

		BannerDto bannerDto = modelMapper.map(banner, BannerDto.class);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Banner sort order updated successfully").data(bannerDto).path(request.getServletPath())
				.build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}

	@DeleteMapping("/banner/{bannerId}")
	public ResponseEntity<SuccessResponseDto> deleteBanner(HttpServletRequest request,
			@PathVariable(name = "bannerId") Long bannerId) {
		Store store = storeService
				.getStore(authenticationService.getAuthenticatedUser(authenticationService.getAuthentication()));

		bannerService.deleteBanner(bannerId, store);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Banner deleted successfully").data(null).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}
}
