package com.coedmaster.vstore.service.contract;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.coedmaster.vstore.domain.Store;
import com.coedmaster.vstore.domain.User;
import com.coedmaster.vstore.dto.StoreDto;

public interface IStoreService {
	Store getStore(Long storeId);

	Store getStore(Long storeId, boolean enabled);

	Store getStore(User user);

	Store getStore(String code);

	Store getStore(String code, boolean enabled);

	Page<Store> getStores(Pageable pageable);

	Page<Store> getStores(boolean enabled, Pageable pageable);

	Store createStore(User user, StoreDto payload);

	Store updateStore(Store store, StoreDto payload);

	boolean isStoreCodeAvailable(String code);

	boolean isStoreCodeAvailableFor(String code, Store store);
}
