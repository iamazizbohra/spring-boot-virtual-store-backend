package com.coedmaster.vstore.service.contract;

import com.coedmaster.vstore.domain.User;

public interface IUserService {
	User getUser(Long userId);
	
	User getUserByUuid(String uuid);
	
	User getUserByMobile(String mobile);
}
