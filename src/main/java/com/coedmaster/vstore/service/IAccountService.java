package com.coedmaster.vstore.service;

import com.coedmaster.vstore.dto.UpdatePasswordDto;
import com.coedmaster.vstore.dto.request.AccountRequestDto;
import com.coedmaster.vstore.model.User;

public interface IAccountService {	
	User createAdminAccount(AccountRequestDto payload);

	User createBuyerAccount(AccountRequestDto payload);

	User createSellerAccount(AccountRequestDto payload);

	User updateAccount(User user, AccountRequestDto payload);

	User updatePassword(User user, UpdatePasswordDto payload);

	boolean isMobileNoAvailable(String mobile);

	boolean isMobileNoAvailableFor(String mobile, User user);
}
