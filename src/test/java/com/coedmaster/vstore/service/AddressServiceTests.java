package com.coedmaster.vstore.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;

import java.util.List;
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

import com.coedmaster.vstore.dto.AddressDto;
import com.coedmaster.vstore.exception.EntityNotFoundException;
import com.coedmaster.vstore.exception.UnallowedOperationException;
import com.coedmaster.vstore.model.Address;
import com.coedmaster.vstore.model.User;
import com.coedmaster.vstore.respository.AddressRepository;
import com.github.javafaker.Faker;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AddressServiceTests {

	@Mock
	private AddressRepository addressRepository;

	@InjectMocks
	private AddressService addressService;

	private final Faker faker = new Faker();

	@Test
	@Order(1)
	@DisplayName("Get address by addressId and userId test")
	public void givenAddressIdAndUser_whenGetAddress_thenReturnAddressOfUser() {
		// given
		User user = new User();
		user.setId(1L);
		user.setUuid(UUID.randomUUID());

		Address address = new Address();
		address.setId(1L);
		address.setUser(user);
		address.setTitle("Home Address");

		given(addressRepository.findByIdAndUserId(1L, user.getId())).willReturn(Optional.of(address));
		given(addressRepository.findByIdAndUserId(2L, user.getId())).willThrow(EntityNotFoundException.class);

		// when
		Address expectedAddress = addressService.getAddress(1L, user);

		// then
		assertThat(expectedAddress).isNotNull();
		assertThrows(EntityNotFoundException.class, () -> addressService.getAddress(2L, user));
	}

	@Test
	@Order(2)
	@DisplayName("Get addresses test")
	public void givenUser_whenGetAddresses_thenReturnAddressesOfUser() {
		// given
		User user1 = new User();
		user1.setId(1L);
		user1.setUuid(UUID.randomUUID());

		User user2 = new User();
		user2.setId(2L);
		user2.setUuid(UUID.randomUUID());

		Address address1 = new Address();
		address1.setId(1L);
		address1.setUser(user1);
		address1.setTitle("Home Address");

		Address address2 = new Address();
		address2.setId(2L);
		address2.setUser(user1);
		address2.setTitle("Office Address");

		given(addressRepository.findAllByUserId(user1.getId())).willReturn(List.of(address1, address2));
		given(addressRepository.findAllByUserId(user2.getId())).willReturn(List.of());

		// when
		List<Address> expectedAddresses1 = addressService.getAddresses(user1);
		List<Address> expectedAddresses2 = addressService.getAddresses(user2);

		// then
		assertThat(expectedAddresses1.size()).isEqualTo(2);
		assertThat(expectedAddresses2.size()).isEqualTo(0);
	}

	@Test
	@Order(3)
	@DisplayName("Create address test")
	public void givenUserAndAddressDto_whenCreateAddress_thenReturnSavedAddress() {
		// given
		User user1 = new User();
		user1.setId(1L);
		user1.setUuid(UUID.randomUUID());

		User user2 = new User();
		user2.setId(2L);
		user2.setUuid(UUID.randomUUID());

		Address address2 = new Address();
		address2.setId(1L);
		address2.setUser(user2);
		address2.setTitle("Home Address");

		AddressDto addressDto = new AddressDto();
		addressDto.setTitle("Home Address");
		addressDto.setName(faker.address().firstName());
		addressDto.setMobile(faker.phoneNumber().phoneNumber());
		addressDto.setState(faker.address().state());
		addressDto.setCity(faker.address().city());
		addressDto.setPincode(faker.address().zipCode());
		addressDto.setLine1(faker.address().fullAddress());
		addressDto.setLine2(faker.address().fullAddress());
		addressDto.setLandmark(faker.address().streetName());

		given(addressRepository.findAllByUserId(user1.getId())).willReturn(List.of());
		given(addressRepository.findAllByUserId(user2.getId())).willReturn(List.of(address2));
		given(addressRepository.save(any(Address.class))).willReturn(new Address());

		// then
		Address expectedAddress1 = addressService.createAddress(user1, addressDto);
		Address expectedAddress2 = addressService.createAddress(user2, addressDto);

		// when
		assertThat(expectedAddress1).isNotNull();
		assertThat(expectedAddress2).isNotNull();
		then(addressRepository).should(times(2)).save(argThat(address -> {
			if (address.getUser().getId() == 1L && address.isDefault() == true) {
				return true;
			} else if (address.getUser().getId() == 2L && address.isDefault() == false) {
				return true;
			}

			return false;
		}));
	}

	@Test
	@Order(4)
	@DisplayName("Update address test")
	public void givenAddressIdUserAndAddressDto_whenUpdateAddress_thenReturnUpdatedAddress() {
		// given
		User user = new User();
		user.setId(1L);
		user.setUuid(UUID.randomUUID());

		Address address = new Address();
		address.setId(1L);
		address.setUser(user);
		address.setTitle("Office Address");

		AddressDto addressDto = new AddressDto();
		addressDto.setTitle("Home Address");
		addressDto.setName(faker.address().firstName());
		addressDto.setMobile(faker.phoneNumber().phoneNumber());
		addressDto.setState(faker.address().state());
		addressDto.setCity(faker.address().city());
		addressDto.setPincode(faker.address().zipCode());
		addressDto.setLine1(faker.address().fullAddress());
		addressDto.setLine2(faker.address().fullAddress());
		addressDto.setLandmark(faker.address().streetName());

		given(addressRepository.findByIdAndUserId(address.getId(), user.getId())).willReturn(Optional.of(address));
		given(addressRepository.save(any(Address.class))).willReturn(new Address());

		// when
		Address expectedAddress = addressService.updateAddress(address.getId(), user, addressDto);

		// then
		assertThat(expectedAddress).isNotNull();
		then(addressRepository).should(times(1)).save(any(Address.class));
	}

	@Test
	@Order(5)
	@DisplayName("Update default address test")
	public void givenAddressIdUser_whenUpdateDefaultAddress_thenReturnUpdatedAddress() {
		// given
		User user = new User();
		user.setId(1L);
		user.setUuid(UUID.randomUUID());

		Address address1 = new Address();
		address1.setId(1L);
		address1.setUser(user);
		address1.setTitle("Home Address");
		address1.setDefault(true);

		Address address2 = new Address();
		address2.setId(2L);
		address2.setUser(user);
		address2.setTitle("Office Address");
		address2.setDefault(false);

		given(addressRepository.findAllByUserId(user.getId())).willReturn(List.of(address1, address2));
		given(addressRepository.findByIdAndUserId(address2.getId(), user.getId())).willReturn(Optional.of(address2));
		given(addressRepository.save(any(Address.class))).willReturn(new Address());

		// when
		Address expectedAddress = addressService.updateDefaultAddress(address2.getId(), user);

		// then
		assertThat(expectedAddress).isNotNull();
		then(addressRepository).should(times(3)).save(any(Address.class));
	}

	@Test
	@Order(6)
	@DisplayName("Delete address test")
	public void givenAddressIdUser_whenDeleteAddress_thenRemoveAddress() {
		// given
		User user = new User();
		user.setId(1L);
		user.setUuid(UUID.randomUUID());

		Address address1 = new Address();
		address1.setId(1L);
		address1.setUser(user);
		address1.setTitle("Home Address");
		address1.setDefault(true);

		Address address2 = new Address();
		address2.setId(2L);
		address2.setUser(user);
		address2.setTitle("Office Address");
		address2.setDefault(false);

		given(addressRepository.findByIdAndUserId(address1.getId(), user.getId())).willReturn(Optional.of(address1));
		given(addressRepository.findByIdAndUserId(address2.getId(), user.getId())).willReturn(Optional.of(address2));
		doNothing().when(addressRepository).delete(any(Address.class));

		// when
		addressService.deleteAddress(address2.getId(), user);

		// then
		assertThrows(UnallowedOperationException.class, () -> addressService.deleteAddress(address1.getId(), user));
		then(addressRepository).should().delete(address2);
	}

}
