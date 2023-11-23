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
import com.coedmaster.vstore.service.BannerService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

@RestController
@RequestMapping("/seller")
public class BannerController {
	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private Validator validator;

	@Autowired
	private BannerService bannerService;

	@GetMapping("/banner")
	public ResponseEntity<SuccessResponseDto> getBanners(HttpServletRequest request) {
		List<Banner> banners = bannerService.getBanners();

		List<BannerResponseDto> bannerResponseDto = banners.stream()
				.map(e -> modelMapper.map(e, BannerResponseDto.class)).collect(Collectors.toList());

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Banners fetched successful").data(bannerResponseDto).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}

	@PostMapping("/banner")
	public ResponseEntity<SuccessResponseDto> createBanner(HttpServletRequest request,
			@RequestBody BannerRequestDto payload) {
		Set<ConstraintViolation<BannerRequestDto>> violations = validator.validate(payload);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException("Constraint violation", violations);
		}

		Banner banner = bannerService.createBanner(payload);

		BannerResponseDto bannerResponseDto = modelMapper.map(banner, BannerResponseDto.class);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Banner created successful").data(bannerResponseDto).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);

	}

	@PutMapping("/banner/{id}")
	public ResponseEntity<SuccessResponseDto> updateBanner(HttpServletRequest request,
			@PathVariable(value = "id") Long id, @RequestBody BannerRequestDto payload) {
		Set<ConstraintViolation<BannerRequestDto>> violations = validator.validate(payload);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException("Constraint violation", violations);
		}

		Banner banner = bannerService.updateBanner(id, payload);

		BannerResponseDto bannerResponseDto = modelMapper.map(banner, BannerResponseDto.class);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Banner updated successful").data(bannerResponseDto).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}

	@DeleteMapping("/banner/{id}")
	public ResponseEntity<SuccessResponseDto> deleteBanner(HttpServletRequest request,
			@PathVariable(value = "id") Long id) {

		bannerService.deleteBanner(id);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Banner deleted successful").data(null).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}

	@PatchMapping("/banner/{id}/status")
	public ResponseEntity<SuccessResponseDto> updateBannerStatus(HttpServletRequest request,
			@PathVariable(value = "id") Long id, @RequestBody UpdateStatusDto payload) {
		Set<ConstraintViolation<UpdateStatusDto>> violations = validator.validate(payload);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException("Constraint violation", violations);
		}

		Banner banner = bannerService.updateBannerStatus(id, payload);

		BannerResponseDto bannerResponseDto = modelMapper.map(banner, BannerResponseDto.class);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Banner status updated successful").data(bannerResponseDto).path(request.getServletPath())
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

		Banner banner = bannerService.updateBannerSortOrder(id, payload);

		BannerResponseDto bannerResponseDto = modelMapper.map(banner, BannerResponseDto.class);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Banner sort order updated successful").data(bannerResponseDto).path(request.getServletPath())
				.build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}
}
