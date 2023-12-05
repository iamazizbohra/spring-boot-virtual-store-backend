package com.coedmaster.vstore.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

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
import com.coedmaster.vstore.model.AuthAccessToken;
import com.coedmaster.vstore.model.Role;
import com.coedmaster.vstore.model.User;
import com.coedmaster.vstore.model.embeddable.FullName;
import com.coedmaster.vstore.respository.AuthAccessTokenRepository;
import com.coedmaster.vstore.respository.RoleRepository;
import com.coedmaster.vstore.respository.UserRepository;
import com.github.javafaker.Faker;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthAccessTokenRepositoryTests {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AuthAccessTokenRepository authAccessTokenRepository;

	private final Faker faker = new Faker();

	private Role role;

	private User user;

	private List<AuthAccessToken> authAccessTokens = new ArrayList<AuthAccessToken>();

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
		user = userRepository.save(user);

		IntStream.range(0, 3).mapToLong(Long::valueOf).forEach((e) -> {
			AuthAccessToken authAccessToken = new AuthAccessToken();
			authAccessToken.setUser(user);
			authAccessToken.setName("Auth Access Token");
			authAccessToken.setToken(Base64.getEncoder().encodeToString("access_token".getBytes()));
			authAccessToken.setExpiresAt(LocalDateTime.now().plusDays(1));
			authAccessTokens.add(authAccessToken);
		});
	}

	@Test
	@Order(1)
	@DisplayName("Save authAccessToken test")
	public void givenAuthAccessToken_whenSave_thenReturnAuthAccessToken() {
		// given
		AuthAccessToken authAccessToken = new AuthAccessToken();
		authAccessToken.setUser(user);
		authAccessToken.setName("Auth Access Token");
		authAccessToken.setToken(Base64.getEncoder().encodeToString("access_token".getBytes()));
		authAccessToken.setExpiresAt(LocalDateTime.now().plusDays(1));

		// when
		AuthAccessToken expectedAuthAccessToken = authAccessTokenRepository.save(authAccessToken);

		// then
		assertAll(() -> assertThat(expectedAuthAccessToken).isNotNull(),
				() -> assertThat(expectedAuthAccessToken.getId()).isGreaterThan(0),
				() -> assertThat(expectedAuthAccessToken.getName()).isEqualTo("Auth Access Token"));
	}

	@Test
	@Order(2)
	@DisplayName("Find all authAccessTokens by userId test")
	public void givenUser_whenFindAllByUserId_thenReturnListOfAuthAccessTokens() {
		// given
		for (AuthAccessToken authAccessToken : authAccessTokens) {
			authAccessTokenRepository.save(authAccessToken);
		}

		// when
		List<AuthAccessToken> expectedAuthAccessTokens = authAccessTokenRepository.findAllByUserId(user.getId());

		// then
		assertThat(expectedAuthAccessTokens.size()).isGreaterThan(0);
	}

	@Test
	@Order(3)
	@DisplayName("Find authAccessToken by token test")
	public void givenToken_whenFindByToken_thenReturnAuthAccessToken() {
		// given
		String token = Base64.getEncoder().encodeToString("access_token".getBytes());

		AuthAccessToken authAccessToken = new AuthAccessToken();
		authAccessToken.setUser(user);
		authAccessToken.setName("Auth Access Token");
		authAccessToken.setToken(token);
		authAccessToken.setExpiresAt(LocalDateTime.now().plusDays(1));
		authAccessTokenRepository.save(authAccessToken);

		// when
		AuthAccessToken expectedAuthAccessToken = authAccessTokenRepository.findByToken(token)
				.orElseThrow(() -> fail("Token not found"));

		// then
		assertAll(() -> assertThat(expectedAuthAccessToken).isNotNull(),
				() -> assertThat(expectedAuthAccessToken.getId()).isGreaterThan(0));
	}

	@Test
	@Order(4)
	@DisplayName("Delete authAccessToken test")
	public void givenAuthAccessToken_whenDelete_thenRemoveAuthAccessToken() {
		// given
		AuthAccessToken authAccessToken = new AuthAccessToken();
		authAccessToken.setUser(user);
		authAccessToken.setName("Auth Access Token");
		authAccessToken.setToken(Base64.getEncoder().encodeToString("access_token".getBytes()));
		authAccessToken.setExpiresAt(LocalDateTime.now().plusDays(1));
		authAccessToken = authAccessTokenRepository.save(authAccessToken);

		// When
		authAccessTokenRepository.delete(authAccessToken);
		Optional<AuthAccessToken> authAccessTokenOptional = authAccessTokenRepository.findById(authAccessToken.getId());

		// then
		assertThat(authAccessTokenOptional).isEmpty();
	}

}
