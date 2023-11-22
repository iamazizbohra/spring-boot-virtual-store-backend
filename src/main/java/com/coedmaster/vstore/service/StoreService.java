package com.coedmaster.vstore.service;

import com.coedmaster.vstore.dto.StoreDto;
import com.coedmaster.vstore.model.Store;

public interface StoreService {
	Store getStoreByCode(String code);

	Store createStore(StoreDto payload);

	Store updateStore(Long id, StoreDto payload);
}
