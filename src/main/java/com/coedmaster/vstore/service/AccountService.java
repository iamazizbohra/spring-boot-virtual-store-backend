package com.coedmaster.vstore.service;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coedmaster.vstore.domain.AuthAccessToken;
import com.coedmaster.vstore.domain.Role;
import com.coedmaster.vstore.domain.User;
import com.coedmaster.vstore.dto.CreateAccountDto;
import com.coedmaster.vstore.dto.UpdateAccountDto;
import com.coedmaster.vstore.dto.UpdatePasswordDto;
import com.coedmaster.vstore.enums.UserRole;
import com.coedmaster.vstore.enums.UserType;
import com.coedmaster.vstore.exception.InvalidMobileVerificationCodeException;
import com.coedmaster.vstore.exception.MobileVerificationCodeNotFoundException;
import com.coedmaster.vstore.exception.PasswordMismatchException;
import com.coedmaster.vstore.exception.UsernameAlreadyTakenException;
import com.coedmaster.vstore.service.contract.IAccountService;
import com.coedmaster.vstore.service.contract.IUserService;

import jakarta.transaction.Transactional;

@Service
public class AccountService implements IAccountService {

	@Autowired
	private IUserService userService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private MobileVerificationService mobileVerificationService;

	@Override
	public User createAdminAccount(CreateAccountDto payload) {
		Role role = roleService.getRoleByName(UserRole.ROLE_ADMIN.name());

		return createAccount(UserType.ADMIN, role, payload);
	}

	@Override
	public User createBuyerAccount(CreateAccountDto payload) {
		Role role = roleService.getRoleByName(UserRole.ROLE_BUYER.name());

		return createAccount(UserType.BUYER, role, payload);
	}

	@Override
	public User createSellerAccount(CreateAccountDto payload) {
		Role role = roleService.getRoleByName(UserRole.ROLE_SELLER.name());

		return createAccount(UserType.SELLER, role, payload);
	}

	@Transactional
	private User createAccount(UserType userType, Role role, CreateAccountDto payload) {
		if (!userService.isMobileAvailable(payload.getMobile())) {
			throw new UsernameAlreadyTakenException("Mobile no is already taken");
		}

		if (ObjectUtils.isEmpty(payload.getVerificationCode())) {
			mobileVerificationService.initVerification(payload.getMobile());

			throw new MobileVerificationCodeNotFoundException("Mobile verification code not found");
		}

		if (ObjectUtils.isNotEmpty(payload.getVerificationCode())
				&& !mobileVerificationService.verifyCode(payload.getMobile(), payload.getVerificationCode())) {
			throw new InvalidMobileVerificationCodeException("Invalid mobile verification code");
		}

		return userService.createUser(userType, role, payload);
	}

	@Override
	@Transactional
	public User updateAccount(User user, UpdateAccountDto payload) {
		if (!StringUtils.equals(user.getMobile(), payload.getMobile())
				&& !userService.isMobileAvailableFor(payload.getMobile(), user)) {
			throw new UsernameAlreadyTakenException("Mobile no is already taken");
		}

		if (!StringUtils.equals(user.getMobile(), payload.getMobile())
				&& ObjectUtils.isEmpty(payload.getVerificationCode())) {
			mobileVerificationService.initVerification(payload.getMobile());

			throw new MobileVerificationCodeNotFoundException("Mobile verification code not found");
		}

		if (!StringUtils.equals(user.getMobile(), payload.getMobile())
				&& ObjectUtils.isNotEmpty(payload.getVerificationCode())
				&& !mobileVerificationService.verifyCode(payload.getMobile(), payload.getVerificationCode())) {
			throw new InvalidMobileVerificationCodeException("Invalid mobile verification code");
		}

		return userService.updateUser(user, payload);
	}

	@Override
	@Transactional
	public AuthAccessToken updatePassword(User user, UpdatePasswordDto payload) {
		if (!authenticationService.verifyPassword(user, payload.getCurrentPassword()))
			throw new PasswordMismatchException("Current password does not match");

		userService.updateUserPassword(user, payload.getNewPassword());

		authenticationService.deleteAllTokens(user);

		AuthAccessToken authAccessToken = authenticationService.generateToken(user);

		return authAccessToken;
	}

}
