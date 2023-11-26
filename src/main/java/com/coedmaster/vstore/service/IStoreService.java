package com.coedmaster.vstore.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.coedmaster.vstore.dto.StoreDto;
import com.coedmaster.vstore.model.Store;
import com.coedmaster.vstore.model.User;

public interface IStoreService {
	Store getStoreById(Long storeId);

	Store getStoreByCode(String code);

	Store getStoreByUser(User user);

	Page<Store> getStores(Pageable pageable);

	Store createStore(User user, StoreDto payload);

	Store updateStore(Store store, StoreDto payload);

	boolean isStoreCodeAvailable(String code);

	boolean isStoreCodeAvailableFor(String code, Store store);
}
