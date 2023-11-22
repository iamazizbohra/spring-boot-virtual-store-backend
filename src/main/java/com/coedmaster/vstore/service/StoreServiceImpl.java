package com.coedmaster.vstore.service;

import java.util.Optional;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.coedmaster.vstore.dto.StoreDto;
import com.coedmaster.vstore.exception.EntityAlreadyExistsException;
import com.coedmaster.vstore.exception.EntityNotFoundException;
import com.coedmaster.vstore.exception.StoreCodeAlreadyTakenException;
import com.coedmaster.vstore.model.Store;
import com.coedmaster.vstore.model.User;
import com.coedmaster.vstore.model.UserDetailsImpl;
import com.coedmaster.vstore.respository.StoreRepository;
import com.coedmaster.vstore.respository.UserRepository;

@Service
public class StoreServiceImpl implements StoreService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private StoreRepository storeRepository;

	@Override
	public Store getStoreByCode(String code) {
		Store store = storeRepository.findByCode(code)
				.orElseThrow(() -> new EntityNotFoundException("Store not found"));

		return store;
	}

	@Override
	public Store createStore(StoreDto payload) {
		UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();

		User user = userRepository.findById(userDetails.getId())
				.orElseThrow(() -> new UsernameNotFoundException("User principal not found"));
		if (!ObjectUtils.isEmpty(user.getStore())) {
			throw new EntityAlreadyExistsException("Store already exists with your account");
		}

		Optional<Store> storeOptional = storeRepository.findByCode(payload.getCode());
		if (!storeOptional.isEmpty())
			throw new StoreCodeAlreadyTakenException("Store code is already taken");

		Store store = Store.builder().user(user).name(payload.getName()).code(payload.getCode())
				.mobile(payload.getMobile()).whatsapp(payload.getWhatsapp()).email(payload.getEmail())
				.latitude(payload.getLatitude()).longitude(payload.getLongitude()).address(payload.getAddress())
				.enabled(true).build();

		return storeRepository.save(store);
	}

	@Override
	public Store updateStore(Long id, StoreDto payload) {
		Optional<Store> storeOptional = storeRepository.findByCode(payload.getCode());
		if (!storeOptional.isEmpty()) {
			Store store = storeOptional.get();
			if (!store.getId().equals(id))
				throw new StoreCodeAlreadyTakenException("Store code is already taken");
		}

		Store store = storeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Store not found"));
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

}
