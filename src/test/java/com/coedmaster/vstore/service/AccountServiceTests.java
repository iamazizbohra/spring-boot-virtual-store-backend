package com.coedmaster.vstore.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.coedmaster.vstore.dto.CreateAccountDto;
import com.coedmaster.vstore.dto.UpdateAccountDto;
import com.coedmaster.vstore.dto.UpdatePasswordDto;
import com.coedmaster.vstore.enums.UserRole;
import com.coedmaster.vstore.enums.UserType;
import com.coedmaster.vstore.exception.InvalidMobileVerificationCodeException;
import com.coedmaster.vstore.exception.MobileVerificationCodeNotFoundException;
import com.coedmaster.vstore.exception.PasswordMismatchException;
import com.coedmaster.vstore.exception.UsernameAlreadyTakenException;
import com.coedmaster.vstore.model.AuthAccessToken;
import com.coedmaster.vstore.model.Role;
import com.coedmaster.vstore.model.User;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccountServiceTests {

	@Mock
	private UserService userService;

	@Mock
	private RoleService roleService;

	@Mock
	private AuthenticationService authenticationService;

	@Mock
	private MobileVerificationService mobileVerificationService;

	@InjectMocks
	private AccountService accountService;

	private CreateAccountDto createAccountDto;

	private CreateAccountDto validCreateAccountDto;

	@BeforeEach
	public void beforeEach() {
		createAccountDto = new CreateAccountDto();
		createAccountDto.setMobile("0123456789");
		createAccountDto.setVerificationCode("1234");
		createAccountDto.setPassword("12345678");

		validCreateAccountDto = new CreateAccountDto();
		validCreateAccountDto.setMobile("9999999999");
		validCreateAccountDto.setVerificationCode("4321");
		validCreateAccountDto.setPassword("12345678");
	}

	@Test
	@Order(1)
	@DisplayName("Create account test")
	public void givenCreateAccountDto_whenCreateAccount_thenReturnCreatedAccount() {
		// given
		Role role = new Role();
		role.setId(1L);

		given(roleService.getRoleByName(UserRole.ROLE_ADMIN.name())).willReturn(role);
		given(roleService.getRoleByName(UserRole.ROLE_BUYER.name())).willReturn(role);
		given(roleService.getRoleByName(UserRole.ROLE_SELLER.name())).willReturn(role);

		given(userService.isMobileAvailable("0123456789")).willReturn(false);
		given(userService.isMobileAvailable("9999999999")).willReturn(true);

		doNothing().when(mobileVerificationService).initVerification("9999999999");

		given(mobileVerificationService.verifyCode("9999999999", "1234")).willReturn(false);
		given(mobileVerificationService.verifyCode("9999999999", "4321")).willReturn(true);
		given(userService.createUser(any(UserType.class), any(Role.class), any(CreateAccountDto.class)))
				.willReturn(new User());

		// when
		User expectedUser1 = accountService.createAdminAccount(validCreateAccountDto);
		User expectedUser2 = accountService.createBuyerAccount(validCreateAccountDto);
		User expectedUser3 = accountService.createSellerAccount(validCreateAccountDto);

		// then
		assertThat(expectedUser1).isNotNull();
		assertThrows(UsernameAlreadyTakenException.class, () -> accountService.createAdminAccount(createAccountDto));

		assertThat(expectedUser2).isNotNull();
		assertThrows(UsernameAlreadyTakenException.class, () -> accountService.createBuyerAccount(createAccountDto));

		assertThat(expectedUser3).isNotNull();
		assertThrows(UsernameAlreadyTakenException.class, () -> accountService.createSellerAccount(createAccountDto));

		createAccountDto.setMobile("9999999999");
		createAccountDto.setVerificationCode("");
		assertThrows(MobileVerificationCodeNotFoundException.class,
				() -> accountService.createAdminAccount(createAccountDto));

		createAccountDto.setMobile("9999999999");
		createAccountDto.setVerificationCode("");
		assertThrows(MobileVerificationCodeNotFoundException.class,
				() -> accountService.createBuyerAccount(createAccountDto));

		createAccountDto.setMobile("9999999999");
		createAccountDto.setVerificationCode("");
		assertThrows(MobileVerificationCodeNotFoundException.class,
				() -> accountService.createSellerAccount(createAccountDto));

		createAccountDto.setMobile("9999999999");
		createAccountDto.setVerificationCode("1234");
		assertThrows(InvalidMobileVerificationCodeException.class,
				() -> accountService.createAdminAccount(createAccountDto));

		createAccountDto.setMobile("9999999999");
		createAccountDto.setVerificationCode("1234");
		assertThrows(InvalidMobileVerificationCodeException.class,
				() -> accountService.createBuyerAccount(createAccountDto));

		createAccountDto.setMobile("9999999999");
		createAccountDto.setVerificationCode("1234");
		assertThrows(InvalidMobileVerificationCodeException.class,
				() -> accountService.createSellerAccount(createAccountDto));

		then(userService).should(times(3)).createUser(any(UserType.class), any(Role.class),
				any(CreateAccountDto.class));
	}

	@Test
	@Order(2)
	@DisplayName("Update account test")
	public void givenUserAndUpdateAccountDto_whenUpdateAccount_thenReturnUpdatedAccount() {
		// given
		User user1 = new User();
		user1.setId(1L);
		user1.setUuid(UUID.randomUUID());
		user1.setMobile("1111111111");

		User user2 = new User();
		user2.setId(2L);
		user2.setUuid(UUID.randomUUID());
		user2.setMobile("2222222222");

		UpdateAccountDto updateAccountDto = new UpdateAccountDto();
		updateAccountDto.setMobile("2222222222");
		updateAccountDto.setVerificationCode("1234");

		UpdateAccountDto validUpdateAccountDto = new UpdateAccountDto();
		validUpdateAccountDto.setMobile("3333333333");
		validUpdateAccountDto.setVerificationCode("4321");

		given(userService.isMobileAvailableFor("2222222222", user1)).willReturn(false);
		given(userService.isMobileAvailableFor("3333333333", user1)).willReturn(true);

		doNothing().when(mobileVerificationService).initVerification("3333333333");

		given(mobileVerificationService.verifyCode("3333333333", "1234")).willReturn(false);
		given(mobileVerificationService.verifyCode("3333333333", "4321")).willReturn(true);
		given(userService.updateUser(any(User.class), any(UpdateAccountDto.class))).willReturn(new User());

		// when
		User expectedUser = accountService.updateAccount(user1, validUpdateAccountDto);

		// then
		assertThat(expectedUser).isNotNull();
		assertThrows(UsernameAlreadyTakenException.class, () -> accountService.updateAccount(user1, updateAccountDto));

		updateAccountDto.setMobile("3333333333");
		updateAccountDto.setVerificationCode("");
		assertThrows(MobileVerificationCodeNotFoundException.class,
				() -> accountService.updateAccount(user1, updateAccountDto));

		updateAccountDto.setMobile("3333333333");
		updateAccountDto.setVerificationCode("1234");
		assertThrows(InvalidMobileVerificationCodeException.class,
				() -> accountService.updateAccount(user1, updateAccountDto));

		then(userService).should().updateUser(any(User.class), any(UpdateAccountDto.class));
	}

	@Test
	@Order(3)
	@DisplayName("Update password with wrong current password test")
	public void givenUserAndUpdatePasswordDto_whenUpdatePassword_thenThrowException() {
		// given
		User user1 = new User();
		user1.setId(1L);
		user1.setUuid(UUID.randomUUID());
		user1.setMobile("1111111111");
		user1.setPassword("12345678");

		UpdatePasswordDto updatePasswordDto = new UpdatePasswordDto();
		updatePasswordDto.setCurrentPassword("00000000");
		updatePasswordDto.setNewPassword("12345678");

		given(authenticationService.verifyPassword(user1, "00000000")).willReturn(false);

		// when

		// then
		assertThrows(PasswordMismatchException.class, () -> accountService.updatePassword(user1, updatePasswordDto));

		then(userService).should(never()).updateUserPassword(any(User.class), anyString());
		then(authenticationService).should(never()).deleteAllTokens(any(User.class));
		then(authenticationService).should(never()).generateToken(any(User.class));
	}

	@Test
	@Order(4)
	@DisplayName("Update password with valid current password test")
	public void givenUserAndUpdatePasswordDto_whenUpdatePassword_thenReturnAccessToken() {
		// given
		User user1 = new User();
		user1.setId(1L);
		user1.setUuid(UUID.randomUUID());
		user1.setMobile("1111111111");
		user1.setPassword("12345678");

		UpdatePasswordDto updatePasswordDto = new UpdatePasswordDto();
		updatePasswordDto.setCurrentPassword("12345678");
		updatePasswordDto.setNewPassword("87654321");

		given(authenticationService.verifyPassword(user1, "12345678")).willReturn(true);
		given(userService.updateUserPassword(any(User.class), anyString())).willReturn(new User());
		doNothing().when(authenticationService).deleteAllTokens(any(User.class));
		given(authenticationService.generateToken(any(User.class))).willReturn(new AuthAccessToken());

		// when
		AuthAccessToken authAccessToken = accountService.updatePassword(user1, updatePasswordDto);

		// then
		assertThat(authAccessToken).isNotNull();

		then(userService).should().updateUserPassword(any(User.class), anyString());
		then(authenticationService).should().deleteAllTokens(any(User.class));
		then(authenticationService).should().generateToken(any(User.class));
	}

}
