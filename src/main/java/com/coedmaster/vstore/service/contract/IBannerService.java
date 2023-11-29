package com.coedmaster.vstore.service.contract;

import java.util.List;

import com.coedmaster.vstore.domain.Banner;
import com.coedmaster.vstore.domain.Store;
import com.coedmaster.vstore.dto.BannerDto;

public interface IBannerService {
	Banner getBanner(Long bannerId, Store store);

	List<Banner> getBanners(Store store);

	Banner createBanner(Store store, BannerDto payload);

	Banner updateBanner(Long bannerId, Store store, BannerDto payload);

	Banner updateBannerStatus(Long bannerId, Store store, boolean status);

	Banner updateBannerSortOrder(Long bannerId, Store store, Short sortOrder);

	void deleteBanner(Long bannerId, Store store);

}
