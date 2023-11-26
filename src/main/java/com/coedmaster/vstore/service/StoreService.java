package com.coedmaster.vstore.service;

import java.util.Optional;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.coedmaster.vstore.dto.StoreDto;
import com.coedmaster.vstore.exception.EntityAlreadyExistsException;
import com.coedmaster.vstore.exception.EntityNotFoundException;
import com.coedmaster.vstore.exception.StoreCodeAlreadyTakenException;
import com.coedmaster.vstore.model.Store;
import com.coedmaster.vstore.model.User;
import com.coedmaster.vstore.respository.StoreRepository;

@Service
public class StoreService implements IStoreService {

	@Autowired
	private StoreRepository storeRepository;

	@Override
	public Store getStoreById(Long storeId) {
		return storeRepository.findById(storeId).orElseThrow(() -> new EntityNotFoundException("Store not found"));
	}

	@Override
	public Store getStoreByCode(String code) {
		return storeRepository.findByCode(code).orElseThrow(() -> new EntityNotFoundException("Store not found"));
	}

	@Override
	public Store getStoreByUser(User user) {
		return storeRepository.findByUserId(user.getId())
				.orElseThrow(() -> new UsernameNotFoundException("Store not found"));
	}

	@Override
	public Page<Store> getStores(Pageable pageable) {
		return storeRepository.findAll(pageable);
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
