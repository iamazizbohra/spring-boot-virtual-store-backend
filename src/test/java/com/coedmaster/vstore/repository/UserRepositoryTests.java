package com.coedmaster.vstore.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

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
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	private final Faker faker = new Faker();

	private Role role;

	private User user;

	@BeforeEach
	public void beforeEach() {
		role = new Role();
		role.setName("ROLE_ADMIN");
		role = roleRepository.save(role);

		FullName fullName = new FullName();
		fullName.setFirstName(faker.name().firstName());
		fullName.setLastName(faker.name().fullName());

		user = new User();
		user.setUuid(UUID.randomUUID());
		user.setUserType(UserType.ADMIN);
		user.setFullName(fullName);
		user.setMobile(faker.phoneNumber().phoneNumber());
		user.setPassword(faker.internet().password());
		user.setEmail(faker.internet().emailAddress());
		user.setGender(Gender.MALE);
		user.setRoles(Collections.singletonList(role));
		user.setEnabled(true);
	}

	@Test
	@Order(1)
	@DisplayName("Save user test")
	public void givenUser_whenSave_thenReturnUser() {
		// given
		FullName fullName = new FullName();
		fullName.setFirstName(faker.name().firstName());
		fullName.setLastName(faker.name().fullName());

		User user = new User();
		user.setUuid(UUID.randomUUID());
		user.setUserType(UserType.ADMIN);
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
	public void givenUser_whenUpdate_thenReturnUser() {
		// given
		User actualUser = userRepository.save(user);

		// when
		FullName fullName = new FullName();
		fullName.setFirstName(faker.name().firstName());
		fullName.setLastName(faker.name().fullName());

		actualUser.setFullName(fullName);
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
		user = userRepository.save(user);

		// When
		User expectedUser = userRepository.findById(user.getId()).orElseThrow(() -> fail("User not found"));

		// then
		assertAll(() -> assertThat(expectedUser).isNotNull(), () -> assertThat(expectedUser.getId()).isGreaterThan(0));
	}

	@Test
	@Order(4)
	@DisplayName("Find user by UUID test")
	public void givenUuid_whenFindByUuid_thenReturnUser() {
		// given
		user = userRepository.save(user);

		// When
		User expectedUser = userRepository.findByUuid(user.getUuid()).orElseThrow(() -> fail("User not found"));

		// then
		assertAll(() -> assertThat(expectedUser).isNotNull(), () -> assertThat(expectedUser.getId()).isGreaterThan(0));
	}

	@Test
	@Order(5)
	@DisplayName("Find user by mobile test")
	public void givenMobile_whenFindByMobile_thenReturnUser() {
		// given
		user = userRepository.save(user);

		// When
		User expectedUser = userRepository.findByMobile(user.getMobile()).orElseThrow(() -> fail("User not found"));

		// then
		assertAll(() -> assertThat(expectedUser).isNotNull(), () -> assertThat(expectedUser.getId()).isGreaterThan(0));
	}

	@Test
	@Order(6)
	@DisplayName("Delete user test")
	public void givenUser_whenDelete_thenRemoveUser() {
		// given
		user = userRepository.save(user);

		// When
		userRepository.delete(user);
		Optional<User> userOptional = userRepository.findById(user.getId());

		// then
		assertThat(userOptional).isEmpty();
	}

}
