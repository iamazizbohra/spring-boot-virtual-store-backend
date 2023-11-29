package com.coedmaster.vstore.service.contract;

import com.coedmaster.vstore.domain.User;
import com.coedmaster.vstore.dto.CreateOrUpdateAccountDto;
import com.coedmaster.vstore.dto.UpdatePasswordDto;

public interface IAccountService {	
	User createAdminAccount(CreateOrUpdateAccountDto payload);

	User createBuyerAccount(CreateOrUpdateAccountDto payload);

	User createSellerAccount(CreateOrUpdateAccountDto payload);

	User updateAccount(User user, CreateOrUpdateAccountDto payload);

	User updatePassword(User user, UpdatePasswordDto payload);

	boolean isMobileNoAvailable(String mobile);

	boolean isMobileNoAvailableFor(String mobile, User user);
}
