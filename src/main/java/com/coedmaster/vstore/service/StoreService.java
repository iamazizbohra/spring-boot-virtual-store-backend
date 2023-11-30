package com.coedmaster.vstore.service;

import java.util.Optional;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.coedmaster.vstore.domain.Store;
import com.coedmaster.vstore.domain.User;
import com.coedmaster.vstore.domain.specification.StoreSpecs;
import com.coedmaster.vstore.dto.StoreDto;
import com.coedmaster.vstore.exception.EntityAlreadyExistsException;
import com.coedmaster.vstore.exception.EntityNotFoundException;
import com.coedmaster.vstore.exception.StoreCodeAlreadyTakenException;
import com.coedmaster.vstore.respository.StoreRepository;
import com.coedmaster.vstore.service.contract.IStoreService;

@Service
public class StoreService implements IStoreService {

	@Autowired
	private StoreRepository storeRepository;

	@Override
	public Store getStore(Long storeId) {
		return storeRepository.findById(storeId).orElseThrow(() -> new EntityNotFoundException("Store not found"));
	}

	@Override
	public Store getStore(Long storeId, boolean enabled) {
		Specification<Store> specs = Specification.where(StoreSpecs.hasStoreId(storeId))
				.and(StoreSpecs.isEnabled(enabled));

		return storeRepository.findOne(specs).orElseThrow(() -> new EntityNotFoundException("Store not found"));
	}

	@Override
	public Store getStore(User user) {
		return storeRepository.findByUserId(user.getId())
				.orElseThrow(() -> new EntityNotFoundException("Store not found"));
	}

	@Override
	public Store getStore(String code) {
		return storeRepository.findByCode(code).orElseThrow(() -> new EntityNotFoundException("Store not found"));
	}

	@Override
	public Store getStore(String code, boolean enabled) {
		Specification<Store> specs = Specification.where(StoreSpecs.hasCode(code)).and(StoreSpecs.isEnabled(enabled));

		return storeRepository.findOne(specs).orElseThrow(() -> new EntityNotFoundException("Store not found"));
	}

	@Override
	public Page<Store> getStores(Pageable pageable) {
		return storeRepository.findAll(pageable);
	}

	@Override
	public Page<Store> getStores(boolean enabled, Pageable pageable) {
		Specification<Store> specs = Specification.where(StoreSpecs.isEnabled(enabled));

		return storeRepository.findAll(specs, pageable);
	}

	@Override
	public Store createStore(User user, StoreDto payload) {
		if (!ObjectUtils.isEmpty(user.getStore())) {
			throw new EntityAlreadyExistsException("Store already exists with your account");
		}

		if (!isStoreCodeAvailable(payload.getCode()))
			throw new StoreCodeAlreadyTakenException("Store code is already taken");

		Store store = Store.builder().user(user).name(payload.getName()).code(payload.getCode())
				.mobile(payload.getMobile()).whatsapp(payload.getWhatsapp()).email(payload.getEmail())
				.latitude(payload.getLatitude()).longitude(payload.getLongitude()).address(payload.getAddress())
				.enabled(true).build();

		return storeRepository.save(store);
	}

	@Override
	public Store updateStore(Store store, StoreDto payload) {
		if (!isStoreCodeAvailableFor(payload.getCode(), store)) {
			throw new StoreCodeAlreadyTakenException("Store code is already taken");
		}

		store.setName(payload.getName());
		store.setCode(payload.getCode());
		store.setMobile(payload.getMobile());
		store.setWhatsapp(payload.getWhatsapp());
		store.setEmail(payload.getEmail());
		store.setLatitude(payload.getLatitude());
		store.setLongitude(payload.getLongitude());
		store.setAddress(payload.getAddress());

		return storeRepository.save(store);
	}

	@Override
	public boolean isStoreCodeAvailable(String code) {
		Optional<Store> storeOptional = storeRepository.findByCode(code);
		if (!storeOptional.isEmpty())
			return false;

		return true;
	}

	@Override
	public boolean isStoreCodeAvailableFor(String code, Store store) {
		Optional<Store> storeOptional = storeRepository.findByCode(code);
		if (!storeOptional.isEmpty()) {
			if (!storeOptional.get().getId().equals(store.getId()))
				return false;
		}

		return true;
	}

}
