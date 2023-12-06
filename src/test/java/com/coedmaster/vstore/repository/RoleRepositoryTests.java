package com.coedmaster.vstore.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.coedmaster.vstore.model.Role;
import com.coedmaster.vstore.respository.RoleRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class RoleRepositoryTests {

	@Autowired
	private RoleRepository roleRepository;

	@Test
	@DisplayName("Find role by name test")
	public void givenName_whenFindByName_thenReturnRole() {
		// Given: Setup object or precondition
		String name = "ROLE_USER";

		Role actualRole = new Role();
		actualRole.setName(name);
		roleRepository.save(actualRole);

		// When: Action or behavior that we are going to test
		Optional<Role> expectedRole = roleRepository.findByName(name);

		// Then: Verify the output or expected result
		assertAll(() -> assertThat(expectedRole).isNotEmpty(),
				() -> assertThat(expectedRole.get().getId()).isGreaterThan(0),
				() -> assertThat(expectedRole.get().getName()).isEqualTo(name));
	}

}
