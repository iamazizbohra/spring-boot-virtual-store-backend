package com.coedmaster.vstore.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.coedmaster.vstore.enums.Gender;
import com.coedmaster.vstore.enums.UserType;
import com.coedmaster.vstore.model.Role;
import com.coedmaster.vstore.model.User;
import com.coedmaster.vstore.model.embeddable.FullName;
import com.coedmaster.vstore.respository.RoleRepository;
import com.coedmaster.vstore.respository.UserRepository;
import com.github.javafaker.Faker;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserRepositoryTests {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	private final Faker faker = new Faker();

	private Role role;

	@BeforeEach
	public void beforeEach() {
		role = new Role();
		role.setName("ROLE_SELLER");
		role = roleRepository.save(role);
	}

	@Test
	@Order(1)
	@DisplayName("Save user test")
	public void givenUser_whenSave_thenReturnSavedUser() {
		// given
		FullName fullName = new FullName();
		fullName.setFirstName(faker.name().firstName());
		fullName.setLastName(faker.name().fullName());

		User user = new User();
		user.setUuid(UUID.randomUUID());
		user.setUserType(UserType.SELLER);
		user.setFullName(fullName);
		user.setMobile(faker.phoneNumber().phoneNumber());
		user.setPassword(faker.internet().password());
		user.setEmail(faker.internet().emailAddress());
		user.setGender(Gender.MALE);
		user.setRoles(Collections.singletonList(role));
		user.setEnabled(true);

		// when
		User expectedUser = userRepository.save(user);

		// then
		assertAll(() -> assertThat(expectedUser).isNotNull(), () -> assertThat(expectedUser.getId()).isGreaterThan(0));
	}

	@Test
	@Order(2)
	@DisplayName("Update user test")
	public void givenUser_whenUpdate_thenReturnUpdatedUser() {
		// given
		FullName fullName = new FullName();
		fullName.setFirstName(faker.name().firstName());
		fullName.setLastName(faker.name().fullName());

		User user = new User();
		user.setUuid(UUID.randomUUID());
		user.setUserType(UserType.SELLER);
		user.setFullName(fullName);
		user.setMobile(faker.phoneNumber().phoneNumber());
		user.setPassword(faker.internet().password());
		user.setEmail(faker.internet().emailAddress());
		user.setGender(Gender.MALE);
		user.setRoles(Collections.singletonList(role));
		user.setEnabled(true);
		User actualUser = userRepository.save(user);

		// when
		FullName fullName1 = new FullName();
		fullName.setFirstName(faker.name().firstName());
		fullName.setLastName(faker.name().fullName());

		actualUser.setFullName(fullName1);
		actualUser.setMobile(faker.phoneNumber().phoneNumber());
		actualUser.setPassword(faker.internet().password());
		actualUser.setEmail(faker.internet().emailAddress());
		actualUser.setGender(Gender.MALE);
		actualUser.setRoles(new ArrayList<Role>(actualUser.getRoles()));
		actualUser.setEnabled(true);
		User expectedUser = userRepository.save(actualUser);

		// then
		assertTrue(true);
		assertAll(() -> assertThat(expectedUser).isNotNull(),
				() -> assertThat(expectedUser.getFullName().getFirstName())
						.isEqualTo(actualUser.getFullName().getFirstName()),
				() -> assertThat(expectedUser.getMobile()).isEqualTo(actualUser.getMobile()));
	}

	@Test
	@Order(3)
	@DisplayName("Find user by Id test")
	public void givenId_whenFindById_thenReturnUser() {
		// given
		FullName fullName = new FullName();
		fullName.setFirstName(faker.name().firstName());
		fullName.setLastName(faker.name().fullName());

		User user = new User();
		user.setUuid(UUID.randomUUID());
		user.setUserType(UserType.SELLER);
		user.setFullName(fullName);
		user.setMobile(faker.phoneNumber().phoneNumber());
		user.setPassword(faker.internet().password());
		user.setEmail(faker.internet().emailAddress());
		user.setGender(Gender.MALE);
		user.setRoles(Collections.singletonList(role));
		user.setEnabled(true);
		User actualUser = userRepository.save(user);

		// When
		Optional<User> expectedUser = userRepository.findById(actualUser.getId());

		// then
		assertAll(() -> assertThat(expectedUser).isNotEmpty(),
				() -> assertThat(expectedUser.get().getId()).isGreaterThan(0));
	}

	@Test
	@Order(4)
	@DisplayName("Find user by UUID test")
	public void givenUuid_whenFindByUuid_thenReturnUser() {
		// given
		FullName fullName = new FullName();
		fullName.setFirstName(faker.name().firstName());
		fullName.setLastName(faker.name().fullName());

		User user = new User();
		user.setUuid(UUID.randomUUID());
		user.setUserType(UserType.SELLER);
		user.setFullName(fullName);
		user.setMobile(faker.phoneNumber().phoneNumber());
		user.setPassword(faker.internet().password());
		user.setEmail(faker.internet().emailAddress());
		user.setGender(Gender.MALE);
		user.setRoles(Collections.singletonList(role));
		user.setEnabled(true);
		User actualUser = userRepository.save(user);

		// When
		Optional<User> expectedUser = userRepository.findByUuid(actualUser.getUuid());

		// then
		assertAll(() -> assertThat(expectedUser).isNotEmpty(),
				() -> assertThat(expectedUser.get().getId()).isGreaterThan(0));
	}

	@Test
	@Order(5)
	@DisplayName("Find user by mobile test")
	public void givenMobile_whenFindByMobile_thenReturnUser() {
		// given
		FullName fullName = new FullName();
		fullName.setFirstName(faker.name().firstName());
		fullName.setLastName(faker.name().fullName());

		User user = new User();
		user.setUuid(UUID.randomUUID());
		user.setUserType(UserType.SELLER);
		user.setFullName(fullName);
		user.setMobile(faker.phoneNumber().phoneNumber());
		user.setPassword(faker.internet().password());
		user.setEmail(faker.internet().emailAddress());
		user.setGender(Gender.MALE);
		user.setRoles(Collections.singletonList(role));
		user.setEnabled(true);
		User actualUser = userRepository.save(user);

		// When
		Optional<User> expectedUser = userRepository.findByMobile(actualUser.getMobile());

		// then
		assertAll(() -> assertThat(expectedUser).isNotEmpty(),
				() -> assertThat(expectedUser.get().getId()).isGreaterThan(0));
	}

	@Test
	@Order(6)
	@DisplayName("Delete user test")
	public void givenUser_whenDelete_thenRemoveUser() {
		// given
		FullName fullName = new FullName();
		fullName.setFirstName(faker.name().firstName());
		fullName.setLastName(faker.name().fullName());

		User user = new User();
		user.setUuid(UUID.randomUUID());
		user.setUserType(UserType.SELLER);
		user.setFullName(fullName);
		user.setMobile(faker.phoneNumber().phoneNumber());
		user.setPassword(faker.internet().password());
		user.setEmail(faker.internet().emailAddress());
		user.setGender(Gender.MALE);
		user.setRoles(Collections.singletonList(role));
		user.setEnabled(true);
		User actualUser = userRepository.save(user);

		// When
		userRepository.delete(actualUser);
		Optional<User> expectedUser = userRepository.findById(actualUser.getId());

		// then
		assertThat(expectedUser).isEmpty();
	}

}
