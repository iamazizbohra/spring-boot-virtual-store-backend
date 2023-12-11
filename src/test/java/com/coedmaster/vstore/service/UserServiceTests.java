package com.coedmaster.vstore.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.coedmaster.vstore.dto.CreateUserDto;
import com.coedmaster.vstore.dto.UpdateUserDto;
import com.coedmaster.vstore.enums.Gender;
import com.coedmaster.vstore.enums.UserType;
import com.coedmaster.vstore.exception.EntityNotFoundException;
import com.coedmaster.vstore.exception.UsernameAlreadyTakenException;
import com.coedmaster.vstore.model.Role;
import com.coedmaster.vstore.model.User;
import com.coedmaster.vstore.respository.UserRepository;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceTests {

	@Mock
	private UserRepository userRepository;

	@Mock
	PasswordEncoder passwordEncoder;

	@InjectMocks
	private UserService userService;

	@Test
	@Order(1)
	@DisplayName("Get user by Id test")
	public void givenUserId_whenGetUser_thenReturnUser() {
		// given
		User user = new User();
		user.setId(1L);

		given(userRepository.findById(1L)).willReturn(Optional.of(user));
		given(userRepository.findById(2L)).willThrow(EntityNotFoundException.class);

		// when
		User expectedUser = userService.getUser(1L);
		Throwable thrown = catchThrowable(() -> userRepository.findById(2L));

		// then
		assertThat(expectedUser).isNotNull();
		assertThat(thrown).isInstanceOf(EntityNotFoundException.class);
	}

	@Test
	@Order(2)
	@DisplayName("Get user by UUID test")
	public void givenUuid_whenGetUserByUuid_thenReturnUser() {
		// given
		UUID uuid = UUID.randomUUID();

		User user = new User();
		user.setId(1L);
		user.setUuid(UUID.randomUUID());

		given(userRepository.findByUuid(user.getUuid())).willReturn(Optional.of(user));
		given(userRepository.findByUuid(uuid)).willThrow(EntityNotFoundException.class);

		// when
		User expectedUser = userService.getUserByUuid(user.getUuid().toString());
		Throwable thrown = catchThrowable(() -> userRepository.findByUuid(uuid));

		// then
		assertThat(expectedUser).isNotNull();
		assertThat(thrown).isInstanceOf(EntityNotFoundException.class);
	}

	@Test
	@Order(3)
	@DisplayName("Get user by mobile test")
	public void givenMobile_whenGetUserByMobile_thenReturnUser() {
		// given
		String mobile = "0123456789";

		User user = new User();
		user.setId(1L);
		user.setUuid(UUID.randomUUID());
		user.setMobile("999999999");

		given(userRepository.findByMobile(user.getMobile())).willReturn(Optional.of(user));
		given(userRepository.findByMobile(mobile)).willThrow(EntityNotFoundException.class);

		// when
		User expectedUser = userService.getUserByMobile(user.getMobile());
		Throwable thrown = catchThrowable(() -> userRepository.findByMobile(mobile));

		// then
		assertThat(expectedUser).isNotNull();
		assertThat(thrown).isInstanceOf(EntityNotFoundException.class);
	}

	@Test
	@Order(4)
	@DisplayName("Find is mobile available test")
	public void givenMobile_whenIsMobileAvailable_thenReturnBoolean() {
		// given
		String mobile = "0123456789";

		User user = new User();
		user.setId(1L);
		user.setUuid(UUID.randomUUID());
		user.setMobile("999999999");

		given(userRepository.findByMobile(user.getMobile())).willReturn(Optional.of(user));
		given(userRepository.findByMobile(mobile)).willReturn(Optional.ofNullable(null));

		// when
		boolean expectedValue1 = userService.isMobileAvailable(user.getMobile());
		boolean expectedValue2 = userService.isMobileAvailable(mobile);

		// then
		assertThat(expectedValue1).isEqualTo(false);
		assertThat(expectedValue2).isEqualTo(true);
	}

	@Test
	@Order(5)
	@DisplayName("Find is mobile available for user test")
	public void givenMobileAndUser_whenIsMobileAvailableForUser_thenReturnBoolean() {
		// given
		String mobile = "0123456789";

		User user = new User();
		user.setId(1L);
		user.setUuid(UUID.randomUUID());
		user.setMobile("999999999");

		User user1 = new User();
		user1.setId(2L);
		user.setUuid(UUID.randomUUID());
		user.setMobile("8888888888");

		given(userRepository.findByMobile(user.getMobile())).willReturn(Optional.of(user));
		given(userRepository.findByMobile(mobile)).willReturn(Optional.ofNullable(null));

		// when
		boolean expectedValue1 = userService.isMobileAvailableFor(user.getMobile(), user);
		boolean expectedValue2 = userService.isMobileAvailableFor(mobile, user);
		Throwable thrown = catchThrowable(() -> userService.isMobileAvailableFor(user.getMobile(), user1));

		// then
		assertThat(expectedValue1).isEqualTo(true);
		assertThat(expectedValue2).isEqualTo(true);
		assertThat(thrown).isInstanceOf(UsernameAlreadyTakenException.class);
	}

	@Test
	@Order(6)
	@DisplayName("Create user test")
	public void givenUserTypeAndRoleAndCreateUserDto_whenCreateUser_thenReturnSavedUser() {
		// given
		Role role = new Role();
		role.setId(1L);
		role.setName("ROLE_ADMIN");

		CreateUserDto createUserDto = new CreateUserDto();
		createUserDto.setFirstName("Lorem");
		createUserDto.setLastName("Ipsum");
		createUserDto.setMobile("0123456789");
		createUserDto.setPassword("12345678");
		createUserDto.setEmail("dummy@gmail.com");
		createUserDto.setGender(Gender.MALE.name());

		given(passwordEncoder.encode(anyString())).willReturn(createUserDto.getPassword());
		given(userRepository.save(any(User.class))).willReturn(new User());

		// when
		User expectedUser = userService.createUser(UserType.ADMIN, role, createUserDto);

		// then
		assertThat(expectedUser).isNotNull();
		then(userRepository).should().save(any(User.class));
	}

	@Test
	@Order(7)
	@DisplayName("Update user test")
	public void givenUserAndUpdateUserDto_whenUpdateUser_thenReturnUpdatedUser() {
		// given
		User user = new User();
		user.setId(1L);
		user.setUuid(UUID.randomUUID());
		user.setMobile("0123456789");

		UpdateUserDto updateUserDto = new UpdateUserDto();
		updateUserDto.setFirstName("Lorem");
		updateUserDto.setLastName("Ipsum");
		updateUserDto.setMobile("0123456789");
		updateUserDto.setEmail("dummy@gmail.com");
		updateUserDto.setGender(Gender.MALE.name());

		given(userRepository.save(any(User.class))).willReturn(user);

		// when
		User expectedUser = userService.updateUser(user, updateUserDto);

		// then
		assertThat(expectedUser).isNotNull();
		then(userRepository).should().save(user);
	}

	@Test
	@Order(8)
	@DisplayName("Update password test")
	public void givenUserAndPassword_whenUpdateUserPassword_thenReturnUpdatedUser() {
		// given
		User user = new User();
		user.setId(1L);
		user.setUuid(UUID.randomUUID());
		user.setPassword("12345678");

		String password = "87654321";

		given(passwordEncoder.encode(password)).willReturn(password);
		given(userRepository.save(user)).willReturn(user);

		// when
		User expectedUser = userService.updateUserPassword(user, password);

		// then
		assertThat(expectedUser).isNotNull();
		then(userRepository).should().save(user);
	}

}
