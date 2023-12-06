package com.coedmaster.vstore.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Collections;
import java.util.LinkedList;
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
	private AuthAccessTokenRepository tokenRepository;

	private final Faker faker = new Faker();

	private Role role;

	private List<User> users = new LinkedList<User>();

	@BeforeEach
	public void beforeEach() {
		role = new Role();
		role.setName("ROLE_SELLER");
		role = roleRepository.save(role);

		IntStream.range(0, 2).mapToLong(Long::valueOf).forEach((e) -> {
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
			users.add(userRepository.save(user));
		});

	}

	@Test
	@Order(1)
	@DisplayName("Save token test")
	public void givenToken_whenSave_thenReturnSavedToken() {
		// given
		AuthAccessToken token = new AuthAccessToken();
		token.setUser(users.get(0));
		token.setName("Access Token");
		token.setToken(Base64.getEncoder().encodeToString("access_token".getBytes()));
		token.setExpiresAt(LocalDateTime.now().plusDays(1));

		// when
		AuthAccessToken expectedToken = tokenRepository.save(token);

		// then
		assertAll(() -> assertThat(expectedToken).isNotNull(), () -> assertThat(expectedToken.getId()).isGreaterThan(0),
				() -> assertThat(expectedToken.getName()).isEqualTo("Access Token"));
	}

	@Test
	@Order(2)
	@DisplayName("Find all tokens by userId test")
	public void givenUser_whenFindAllByUserId_thenReturnListOfTokens() {
		// given
		AuthAccessToken token1 = new AuthAccessToken();
		token1.setUser(users.get(0));
		token1.setName("Access Token");
		token1.setToken(Base64.getEncoder().encodeToString("access_token".getBytes()));
		token1.setExpiresAt(LocalDateTime.now().plusDays(1));
		tokenRepository.save(token1);

		IntStream.range(0, 3).mapToLong(Long::valueOf).forEach((e) -> {
			AuthAccessToken token = new AuthAccessToken();
			token.setUser(users.get(1));
			token.setName("Access Token");
			token.setToken(Base64.getEncoder().encodeToString("access_token_".concat(String.valueOf(e)).getBytes()));
			token.setExpiresAt(LocalDateTime.now().plusDays(1));
			tokenRepository.save(token);
		});

		// when
		List<AuthAccessToken> expectedTokens = tokenRepository.findAllByUserId(users.get(1).getId());

		// then
		assertThat(expectedTokens.size()).isEqualTo(3);
	}

	@Test
	@Order(3)
	@DisplayName("Find token by token test")
	public void givenToken_whenFindByToken_thenReturnToken() {
		// given
		AuthAccessToken token1 = new AuthAccessToken();
		token1.setUser(users.get(0));
		token1.setName("Access Token");
		token1.setToken(Base64.getEncoder().encodeToString("access_token".getBytes()));
		token1.setExpiresAt(LocalDateTime.now().plusDays(1));
		tokenRepository.save(token1);

		IntStream.range(0, 3).mapToLong(Long::valueOf).forEach((e) -> {
			AuthAccessToken token = new AuthAccessToken();
			token.setUser(users.get(1));
			token.setName("Access Token");
			token.setToken(Base64.getEncoder().encodeToString("access_token_".concat(String.valueOf(e)).getBytes()));
			token.setExpiresAt(LocalDateTime.now().plusDays(1));
			tokenRepository.save(token);
		});

		// when
		Optional<AuthAccessToken> expectedToken = tokenRepository.findByToken(token1.getToken());

		// then
		assertAll(() -> assertThat(expectedToken).isNotEmpty(),
				() -> assertThat(expectedToken.get().getId()).isGreaterThan(0));
	}

	@Test
	@Order(4)
	@DisplayName("Delete token test")
	public void givenToken_whenDelete_thenRemoveToken() {
		// given
		AuthAccessToken token = new AuthAccessToken();
		token.setUser(users.get(0));
		token.setName("Access Token");
		token.setToken(Base64.getEncoder().encodeToString("access_token".getBytes()));
		token.setExpiresAt(LocalDateTime.now().plusDays(1));
		token = tokenRepository.save(token);

		// When
		tokenRepository.delete(token);
		Optional<AuthAccessToken> tokenOptional = tokenRepository.findById(token.getId());

		// then
		assertThat(tokenOptional).isEmpty();
	}

}
