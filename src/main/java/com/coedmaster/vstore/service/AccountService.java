package com.coedmaster.vstore.service;

import com.coedmaster.vstore.dto.UpdatePasswordDto;
import com.coedmaster.vstore.dto.request.AccountRequestDto;
import com.coedmaster.vstore.model.User;

public interface AccountService {
	User createAdminAccount(AccountRequestDto payload);

	User createBuyerAccount(AccountRequestDto payload);

	User createSellerAccount(AccountRequestDto payload);

	User updateAccount(AccountRequestDto payload);

	User updatePassword(UpdatePasswordDto payload);
}
