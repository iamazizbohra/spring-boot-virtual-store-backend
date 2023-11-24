package com.coedmaster.vstore.service;

import com.coedmaster.vstore.dto.StoreDto;
import com.coedmaster.vstore.model.Store;
import com.coedmaster.vstore.model.User;

public interface IStoreService {
	Store getStoreByUser(User user);

	Store getStoreByCode(String code);

	Store createStore(User user, StoreDto payload);

	Store updateStore(Store store, StoreDto payload);

	boolean isStoreCodeAvailable(String code);

	boolean isStoreCodeAvailableFor(String code, Store store);
}
