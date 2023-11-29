package com.coedmaster.vstore.service.contract;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.coedmaster.vstore.domain.Store;
import com.coedmaster.vstore.domain.User;
import com.coedmaster.vstore.dto.StoreDto;

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
