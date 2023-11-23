package com.coedmaster.vstore.service;

import com.coedmaster.vstore.dto.StoreDto;
import com.coedmaster.vstore.model.Store;

public interface IStoreService {
	Store getStoreByAuthentication();
	
	Store getStoreByCode(String code);

	Store createStore(StoreDto payload);

	Store updateStore(Long id, StoreDto payload);
}
