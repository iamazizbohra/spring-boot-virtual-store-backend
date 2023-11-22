package com.coedmaster.vstore.service;

import com.coedmaster.vstore.dto.AccountDto;
import com.coedmaster.vstore.model.User;

public interface AccountService {
	User createAdminAccount(AccountDto payload);
	
	User createBuyerAccount(AccountDto payload);
	
	User createSellerAccount(AccountDto payload);
}
