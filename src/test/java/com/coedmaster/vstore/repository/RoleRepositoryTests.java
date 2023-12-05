package com.coedmaster.vstore.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.fail;

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
		// given
		String name = "ROLE_ADMIN";

		// when
		Role role = roleRepository.findByName(name).orElseThrow(() -> fail("Role not found"));

		// then
		assertAll(() -> assertThat(role).isNotNull(), () -> assertThat(role.getId()).isGreaterThan(0),
				() -> assertThat(role.getName()).isEqualTo(name));
	}

}
