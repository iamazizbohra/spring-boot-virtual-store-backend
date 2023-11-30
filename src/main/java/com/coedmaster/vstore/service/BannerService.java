package com.coedmaster.vstore.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.coedmaster.vstore.domain.Banner;
import com.coedmaster.vstore.domain.Store;
import com.coedmaster.vstore.dto.BannerDto;
import com.coedmaster.vstore.exception.EntityNotFoundException;
import com.coedmaster.vstore.respository.BannerRepository;
import com.coedmaster.vstore.service.contract.IBannerService;

@Service
public class BannerService implements IBannerService {

	@Autowired
	private BannerRepository bannerRepository;

	@Override
	public Banner getBanner(Long bannerId, Store store) {
		return bannerRepository.findByIdAndStoreId(bannerId, store.getId())
				.orElseThrow(() -> new EntityNotFoundException("Banner not found"));
	}

	@Override
	public List<Banner> getBanners(Store store) {
		return bannerRepository.findAllByStoreId(store.getId(), Sort.by(Sort.Direction.DESC, "sortOrder"));
	}

	@Override
	public List<Banner> getBanners(Store store, boolean enabled) {
		return bannerRepository.findAllByStoreIdAndEnabled(store.getId(), enabled,
				Sort.by(Sort.Direction.DESC, "sortOrder"));
	}

	@Override
	public Banner createBanner(Store store, BannerDto payload) {
		Banner banner = new Banner();
		banner.setStore(store);
		banner.setTitle(payload.getTitle());
		banner.setImage(payload.getImage());
		banner.setSortOrder(Short.valueOf("0"));
		banner.setEnabled(true);

		return bannerRepository.save(banner);
	}

	@Override
	public Banner updateBanner(Long bannerId, Store store, BannerDto payload) {
		Banner banner = getBanner(bannerId, store);
		banner.setTitle(payload.getTitle());
		banner.setImage(payload.getImage());

		return bannerRepository.save(banner);
	}

	@Override
	public void deleteBanner(Long bannerId, Store store) {
		Banner banner = getBanner(bannerId, store);

		bannerRepository.deleteById(banner.getId());
	}

	@Override
	public Banner updateBannerStatus(Long bannerId, Store store, boolean status) {
		Banner banner = getBanner(bannerId, store);
		banner.setEnabled(status);

		return bannerRepository.save(banner);
	}

	@Override
	public Banner updateBannerSortOrder(Long bannerId, Store store, Short sortOrder) {
		Banner banner = getBanner(bannerId, store);
		banner.setSortOrder(sortOrder);

		return bannerRepository.save(banner);
	}
}
