package com.coedmaster.vstore.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

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
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserRepositoryTests {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	private static Faker faker;

	private static User savedUser;

	@BeforeAll
	public static void beforeAllTests() {
		faker = new Faker();
	}

	@Test
	@Order(1)
	@Rollback(value = false)
	@DisplayName("Save user test")
	public void givenUserObject_whenSave_thenReturnSavedUser() {
		// given
		Role role = roleRepository.findByName("ROLE_ADMIN").orElseThrow(() -> fail("Role not found"));

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
		savedUser = userRepository.save(user);

		// then
		assertAll(() -> assertThat(savedUser).isNotNull(), () -> assertThat(savedUser.getId()).isGreaterThan(0));
	}

	@Test
	@Order(2)
	@DisplayName("Find user by Id test")
	public void givenId_whenFindById_thenReturnUser() {
		// given
		Long id = savedUser.getId();

		// When
		User user = userRepository.findById(id).orElseThrow(() -> fail("User not found"));

		// then
		assertAll(() -> assertThat(user).isNotNull(), () -> assertThat(user.getId()).isGreaterThan(0));
	}

	@Test
	@Order(3)
	@DisplayName("Find user by UUID test")
	public void givenUuid_whenFindByUuid_thenReturnUser() {
		// given
		UUID uuid = savedUser.getUuid();

		// When
		User user = userRepository.findByUuid(uuid).orElseThrow(() -> fail("User not found"));

		// then
		assertAll(() -> assertThat(user).isNotNull(), () -> assertThat(user.getId()).isGreaterThan(0));
	}

	@Test
	@Order(4)
	@DisplayName("Find user by mobile test")
	public void givenMobile_whenFindByMobile_thenReturnUser() {
		// given
		String mobile = savedUser.getMobile();

		// When
		User user = userRepository.findByMobile(mobile).orElseThrow(() -> fail("User not found"));

		// then
		assertAll(() -> assertThat(user).isNotNull(), () -> assertThat(user.getId()).isGreaterThan(0));
	}

	@Test
	@Order(5)
	@DisplayName("Delete user test")
	public void givenUser_whenDeleteUser_thenReturnVoid() {
		// given
		User user = savedUser;

		// When
		userRepository.delete(user);

		User deletedUser = null;
		Optional<User> userOptional = userRepository.findById(user.getId());
		if (!userOptional.isEmpty())
			deletedUser = userOptional.get();

		// then
		assertThat(deletedUser).isNull();
	}

}
