package com.coedmaster.vstore.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.coedmaster.vstore.exception.EntityNotFoundException;
import com.coedmaster.vstore.model.Role;
import com.coedmaster.vstore.respository.RoleRepository;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTests {

	@Mock
	private RoleRepository roleRepository;

	@InjectMocks
	private RoleService roleService;

	@Test
	@DisplayName("Get role by name test")
	public void givenRoleName_whenGetRoleByName_thenReturnRole() {
		// given
		Role actualRole = new Role();
		actualRole.setId(1L);
		actualRole.setName("ROLE_ADMIN");

		given(roleRepository.findByName("ROLE_ADMIN")).willReturn(Optional.of(actualRole));
		given(roleRepository.findByName("ROLE_USER")).willThrow(EntityNotFoundException.class);

		// when
		Role expectedRole = roleService.getRoleByName("ROLE_ADMIN");

		// then
		assertThat(expectedRole).isNotNull();
		assertThrows(EntityNotFoundException.class, () -> roleService.getRoleByName("ROLE_USER"));
		then(roleRepository).should(times(2)).findByName(anyString());
	}

}
