package com.coedmaster.vstore.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.coedmaster.vstore.dto.UpdateSortOrderDto;
import com.coedmaster.vstore.dto.UpdateStatusDto;
import com.coedmaster.vstore.dto.request.BannerRequestDto;
import com.coedmaster.vstore.exception.EntityNotFoundException;
import com.coedmaster.vstore.model.Banner;
import com.coedmaster.vstore.model.Store;
import com.coedmaster.vstore.respository.BannerRepository;

@Service
public class BannerService implements IBannerService {

	@Autowired
	private BannerRepository bannerRepository;

	@Override
	public Banner getBanner(Long id, Store store) {
		return bannerRepository.findByIdAndStoreId(id, store.getId())
				.orElseThrow(() -> new EntityNotFoundException("Banner not found"));
	}

	@Override
	public List<Banner> getBanners(Store store) {
		return bannerRepository.findAllByStoreId(store.getId(), Sort.by(Sort.Direction.DESC, "sortOrder"));
	}

	@Override
	public Banner createBanner(Store store, BannerRequestDto payload) {
		Banner banner = new Banner();
		banner.setStore(store);
		banner.setTitle(payload.getTitle());
		banner.setImage(payload.getImage());
		banner.setSortOrder(Short.valueOf("0"));
		banner.setEnabled(true);

		return bannerRepository.save(banner);
	}

	@Override
	public Banner updateBanner(Long id, Store store, BannerRequestDto payload) {
		Banner banner = getBanner(id, store);
		banner.setTitle(payload.getTitle());
		banner.setImage(payload.getImage());

		return bannerRepository.save(banner);
	}

	@Override
	public void deleteBanner(Long id, Store store) {
		Banner banner = getBanner(id, store);

		bannerRepository.deleteById(banner.getId());
	}

	@Override
	public Banner updateBannerStatus(Long id, Store store, UpdateStatusDto payload) {
		Banner banner = getBanner(id, store);
		banner.setEnabled(payload.isEnabled());

		return bannerRepository.save(banner);
	}

	@Override
	public Banner updateBannerSortOrder(Long id, Store store, UpdateSortOrderDto payload) {
		Banner banner = getBanner(id, store);
		banner.setSortOrder(payload.getSortOrder());

		return bannerRepository.save(banner);
	}
}
