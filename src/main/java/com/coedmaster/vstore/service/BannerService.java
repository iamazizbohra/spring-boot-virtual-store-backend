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

	@Autowired
	private StoreServiceImpl storeServiceImpl;

	@Override
	public List<Banner> getBanners() {
		Store store = getStore();

		return bannerRepository.findAllByStoreId(store.getId(), Sort.by(Sort.Direction.DESC, "sortOrder"));
	}

	@Override
	public Banner createBanner(BannerRequestDto payload) {
		Store store = getStore();

		Banner banner = Banner.builder().store(store).title(payload.getTitle()).image(payload.getImage())
				.sortOrder(Short.valueOf("0")).enabled(true).build();

		return bannerRepository.save(banner);
	}

	@Override
	public Banner updateBanner(Long id, BannerRequestDto payload) {
		Banner banner = getBanner(id);

		banner.setTitle(payload.getTitle());
		banner.setImage(payload.getImage());

		return bannerRepository.save(banner);
	}

	@Override
	public void deleteBanner(Long id) {
		Banner banner = getBanner(id);

		bannerRepository.deleteById(banner.getId());
	}

	@Override
	public Banner updateBannerStatus(Long id, UpdateStatusDto payload) {
		Banner banner = getBanner(id);

		banner.setEnabled(payload.isEnabled());

		return bannerRepository.save(banner);
	}

	@Override
	public Banner updateBannerSortOrder(Long id, UpdateSortOrderDto payload) {
		Banner banner = getBanner(id);

		banner.setSortOrder(payload.getSortOrder());

		return bannerRepository.save(banner);
	}

	private Banner getBanner(Long id) {
		Store store = getStore();

		Banner banner = bannerRepository.findByIdAndStoreId(id, store.getId())
				.orElseThrow(() -> new EntityNotFoundException("Banner not found"));

		return banner;
	}

	private Store getStore() {
		return storeServiceImpl.getStoreByAuthentication();
	}

}
