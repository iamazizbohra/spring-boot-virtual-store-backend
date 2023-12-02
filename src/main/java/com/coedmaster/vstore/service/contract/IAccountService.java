package com.coedmaster.vstore.service.contract;

import com.coedmaster.vstore.domain.AuthAccessToken;
import com.coedmaster.vstore.domain.User;
import com.coedmaster.vstore.dto.CreateAccountDto;
import com.coedmaster.vstore.dto.UpdateAccountDto;
import com.coedmaster.vstore.dto.UpdatePasswordDto;

public interface IAccountService {
	User createAdminAccount(CreateAccountDto payload);

	User createBuyerAccount(CreateAccountDto payload);

	User createSellerAccount(CreateAccountDto payload);

	User updateAccount(User user, UpdateAccountDto payload);

	AuthAccessToken updatePassword(User user, UpdatePasswordDto payload);
}
