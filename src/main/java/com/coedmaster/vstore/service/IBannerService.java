package com.coedmaster.vstore.service;

import java.util.List;

import com.coedmaster.vstore.dto.UpdateSortOrderDto;
import com.coedmaster.vstore.dto.UpdateStatusDto;
import com.coedmaster.vstore.dto.request.BannerRequestDto;
import com.coedmaster.vstore.model.Banner;
import com.coedmaster.vstore.model.Store;

public interface IBannerService {
	Banner getBanner(Long id, Store store);

	List<Banner> getBanners(Store store);

	Banner createBanner(Store store, BannerRequestDto payload);

	Banner updateBanner(Long id, Store store, BannerRequestDto payload);

	void deleteBanner(Long id, Store store);

	Banner updateBannerStatus(Long id, Store store, UpdateStatusDto payload);

	Banner updateBannerSortOrder(Long id, Store store, UpdateSortOrderDto payload);

}
