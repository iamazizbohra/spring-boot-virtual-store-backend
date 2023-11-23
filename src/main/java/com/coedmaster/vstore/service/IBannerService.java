package com.coedmaster.vstore.service;

import java.util.List;

import com.coedmaster.vstore.dto.UpdateSortOrderDto;
import com.coedmaster.vstore.dto.UpdateStatusDto;
import com.coedmaster.vstore.dto.request.BannerRequestDto;
import com.coedmaster.vstore.model.Banner;

public interface IBannerService {
	List<Banner> getBanners();

	Banner createBanner(BannerRequestDto payload);

	Banner updateBanner(Long id, BannerRequestDto payload);

	void deleteBanner(Long id);

	Banner updateBannerStatus(Long id, UpdateStatusDto payload);

	Banner updateBannerSortOrder(Long id, UpdateSortOrderDto payload);

}
