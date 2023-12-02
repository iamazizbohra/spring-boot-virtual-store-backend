package com.coedmaster.vstore.controller.buyer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coedmaster.vstore.domain.Banner;
import com.coedmaster.vstore.domain.Store;
import com.coedmaster.vstore.dto.BannerDto;
import com.coedmaster.vstore.dto.response.SuccessResponseDto;
import com.coedmaster.vstore.service.contract.IBannerService;
import com.coedmaster.vstore.service.contract.IStoreService;

import jakarta.servlet.http.HttpServletRequest;

@RestController("BuyerBannerController")
@RequestMapping("/buyer")
public class BannerController {

	@Autowired
	private IStoreService storeService;

	@Autowired
	private IBannerService bannerService;

	@Autowired
	private ModelMapper modelMapper;

	@GetMapping("/store/{storeId}/banner")
	public ResponseEntity<SuccessResponseDto> getBanners(HttpServletRequest request,
			@PathVariable(name = "storeId") Long storeId) {
		Store store = storeService.getStore(storeId);

		List<Banner> banners = bannerService.getBanners(store, true);

		List<BannerDto> bannerDtos = banners.stream().map(e -> modelMapper.map(e, BannerDto.class))
				.collect(Collectors.toList());

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Banners fetched successfully").data(bannerDtos).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}
}
