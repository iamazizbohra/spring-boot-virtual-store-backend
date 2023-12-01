package com.coedmaster.vstore.service;

import java.text.MessageFormat;
import java.time.Duration;
import java.util.Collections;
import java.util.Optional;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.jobrunr.jobs.context.JobContext;
import org.jobrunr.scheduling.BackgroundJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.coedmaster.vstore.domain.Role;
import com.coedmaster.vstore.domain.User;
import com.coedmaster.vstore.domain.embeddable.FullName;
import com.coedmaster.vstore.dto.CreateOrUpdateAccountDto;
import com.coedmaster.vstore.dto.UpdatePasswordDto;
import com.coedmaster.vstore.enums.Gender;
import com.coedmaster.vstore.enums.UserRole;
import com.coedmaster.vstore.enums.UserType;
import com.coedmaster.vstore.exception.EntityNotFoundException;
import com.coedmaster.vstore.exception.InvalidMobileVerificationCodeException;
import com.coedmaster.vstore.exception.MobileVerificationCodeNotFoundException;
import com.coedmaster.vstore.exception.PasswordMismatchException;
import com.coedmaster.vstore.exception.UsernameAlreadyTakenException;
import com.coedmaster.vstore.respository.RoleRepository;
import com.coedmaster.vstore.respository.UserRepository;
import com.coedmaster.vstore.service.contract.IAccountService;
import com.coedmaster.vstore.service.contract.IOtpService;

import jakarta.transaction.Transactional;

@Service
public class AccountService implements IAccountService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	private IOtpService otpService;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	private final int EXPIRE_IN_SECONDS = 180;

	@Override
	public User createAdminAccount(CreateOrUpdateAccountDto payload) {
		Role role = roleRepository.findByName(UserRole.ROLE_ADMIN.name())
				.orElseThrow(() -> new EntityNotFoundException("Role not found"));

		return createAccount(UserType.ADMIN, role, payload);
	}

	@Override
	public User createBuyerAccount(CreateOrUpdateAccountDto payload) {
		Role role = roleRepository.findByName(UserRole.ROLE_BUYER.name())
				.orElseThrow(() -> new EntityNotFoundException("Role not found"));

		return createAccount(UserType.BUYER, role, payload);
	}

	@Override
	public User createSellerAccount(CreateOrUpdateAccountDto payload) {
		Role role = roleRepository.findByName(UserRole.ROLE_SELLER.name())
				.orElseThrow(() -> new EntityNotFoundException("Role not found"));

		return createAccount(UserType.SELLER, role, payload);
	}

	@Transactional
	private User createAccount(UserType userType, Role role, CreateOrUpdateAccountDto payload) {
		if (!isMobileNoAvailable(payload.getMobile())) {
			throw new UsernameAlreadyTakenException("Mobile no is already taken");
		}

		if (ObjectUtils.isEmpty(payload.getVerificationCode())) {
			String verificationCode = otpService.generateRandomPassword(4);

			String key = MessageFormat.format("user:mvc:{0}", payload.getMobile());
			redisTemplate.opsForValue().append(key, verificationCode);
			redisTemplate.expire(key, Duration.ofSeconds(EXPIRE_IN_SECONDS));

			BackgroundJob.<SmsService>enqueue(
					x -> x.sendMobileNumberVerificationMessage(JobContext.Null, payload.getMobile(), verificationCode));

			throw new MobileVerificationCodeNotFoundException("Mobile verification code not found");
		}

		if (ObjectUtils.isNotEmpty(payload.getVerificationCode())) {
			String verificationCode = payload.getVerificationCode();

			String key = MessageFormat.format("user:mvc:{0}", payload.getMobile());
			if (!StringUtils.equals(verificationCode, redisTemplate.opsForValue().get(key))) {
				throw new InvalidMobileVerificationCodeException("Invalid mobile verification code");
			}
		}

		FullName fullName = new FullName();
		fullName.setFirstName(payload.getFirstName());
		fullName.setLastName(payload.getLastName());

		User user = new User();
		user.setUserType(userType);
		user.setFullName(fullName);
		user.setMobile(payload.getMobile());
		user.setPassword(passwordEncoder.encode(payload.getPassword()));
		user.setEmail(payload.getEmail());
		user.setGender(Gender.valueOf(payload.getGender()));
		user.setRoles(Collections.singletonList(role));
		user.setEnabled(true);

		return userRepository.save(user);
	}

	@Override
	public User updateAccount(User user, CreateOrUpdateAccountDto payload) {
		if (!isMobileNoAvailableFor(payload.getMobile(), user)) {
			throw new UsernameAlreadyTakenException("Mobile no is already taken");
		}

		FullName fullName = new FullName();
		fullName.setFirstName(payload.getFirstName());
		fullName.setLastName(payload.getLastName());

		user.setFullName(fullName);
		user.setMobile(payload.getMobile());
		user.setEmail(payload.getEmail());
		user.setGender(Gender.valueOf(payload.getGender()));

		return userRepository.save(user);
	}

	@Override
	public User updatePassword(User user, UpdatePasswordDto payload) {
		if (!passwordEncoder.matches(payload.getCurrentPassword(), user.getPassword()))
			throw new PasswordMismatchException("Current password does not match");

		user.setPassword(passwordEncoder.encode(payload.getNewPassword()));

		return userRepository.save(user);
	}

	@Override
	public boolean isMobileNoAvailable(String mobile) {
		Optional<User> optionalUser = userRepository.findByMobile(mobile);
		if (!optionalUser.isEmpty()) {
			return false;
		}

		return true;
	}

	@Override
	public boolean isMobileNoAvailableFor(String mobile, User user) {
		Optional<User> userOptional = userRepository.findByMobile(mobile);
		if (!userOptional.isEmpty()) {
			if (!userOptional.get().getId().equals(user.getId()))
				throw new UsernameAlreadyTakenException("Mobile no is already taken");
		}

		return true;
	}

}
