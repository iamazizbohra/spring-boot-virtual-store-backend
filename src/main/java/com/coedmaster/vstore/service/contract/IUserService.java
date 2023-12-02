package com.coedmaster.vstore.service.contract;

import com.coedmaster.vstore.domain.Role;
import com.coedmaster.vstore.domain.User;
import com.coedmaster.vstore.dto.CreateUserDto;
import com.coedmaster.vstore.dto.UpdateUserDto;
import com.coedmaster.vstore.enums.UserType;

public interface IUserService {
	User getUser(Long userId);

	User getUserByUuid(String uuid);

	User getUserByMobile(String mobile);

	boolean isMobileAvailable(String mobile);

	boolean isMobileAvailableFor(String mobile, User user);

	User createUser(UserType userType, Role role, CreateUserDto payload);

	User updateUser(User user, UpdateUserDto payload);

	User updateUserPassword(User user, String password);
}
