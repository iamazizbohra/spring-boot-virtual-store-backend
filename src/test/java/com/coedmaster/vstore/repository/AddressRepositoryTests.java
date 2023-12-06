package com.coedmaster.vstore.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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
import com.coedmaster.vstore.model.Address;
import com.coedmaster.vstore.model.Role;
import com.coedmaster.vstore.model.User;
import com.coedmaster.vstore.model.embeddable.FullName;
import com.coedmaster.vstore.respository.AddressRepository;
import com.coedmaster.vstore.respository.RoleRepository;
import com.coedmaster.vstore.respository.UserRepository;
import com.github.javafaker.Faker;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AddressRepositoryTests {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AddressRepository addressRepository;

	private final Faker faker = new Faker();

	private Role role;

	private List<User> users = new LinkedList<User>();

	@BeforeEach
	public void beforeEach() {
		role = new Role();
		role.setName("ROLE_BUYER");
		role = roleRepository.save(role);

		IntStream.range(0, 2).mapToLong(Long::valueOf).forEach((e) -> {
			FullName fullName = new FullName();
			fullName.setFirstName(faker.name().firstName());
			fullName.setLastName(faker.name().fullName());

			User user = new User();
			user.setUuid(UUID.randomUUID());
			user.setUserType(UserType.BUYER);
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
	@DisplayName("Save address test")
	public void givenAddress_whenSave_thenReturnSavedAddress() {
		// given
		Address address = new Address();
		address.setUser(users.get(0));
		address.setTitle("Home Address");
		address.setName(faker.address().firstName());
		address.setMobile(faker.phoneNumber().phoneNumber());
		address.setState(faker.address().state());
		address.setCity(faker.address().city());
		address.setPincode(faker.address().zipCode());
		address.setLine1(faker.address().fullAddress());
		address.setLine2(faker.address().fullAddress());
		address.setLandmark(faker.address().streetName());
		address.setDefault(true);

		// when
		Address expectedAddress = addressRepository.save(address);

		// then
		assertAll(() -> assertThat(expectedAddress).isNotNull(),
				() -> assertThat(expectedAddress.getId()).isGreaterThan(0));
	}

	@Test
	@Order(2)
	@DisplayName("Update address test")
	public void givenAddress_whenUpdate_thenReturnUpdatedAddress() {
		// given
		Address address = new Address();
		address.setUser(users.get(0));
		address.setTitle("Home Address");
		address.setName(faker.address().firstName());
		address.setMobile(faker.phoneNumber().phoneNumber());
		address.setState(faker.address().state());
		address.setCity(faker.address().city());
		address.setPincode(faker.address().zipCode());
		address.setLine1(faker.address().fullAddress());
		address.setLine2(faker.address().fullAddress());
		address.setLandmark(faker.address().streetName());
		address.setDefault(true);
		Address actualAddress = addressRepository.save(address);

		// when
		actualAddress.setTitle("Office Address");
		actualAddress.setName(faker.address().firstName());
		actualAddress.setMobile(faker.phoneNumber().phoneNumber());
		actualAddress.setState(faker.address().state());
		actualAddress.setCity(faker.address().city());
		actualAddress.setPincode(faker.address().zipCode());
		actualAddress.setLine1(faker.address().fullAddress());
		actualAddress.setLine2(faker.address().fullAddress());
		actualAddress.setLandmark(faker.address().streetName());
		actualAddress.setDefault(true);
		Address expectedAddress = addressRepository.save(actualAddress);

		// then
		assertAll(() -> assertThat(expectedAddress).isNotNull(),
				() -> assertThat(expectedAddress.getName()).isEqualTo(actualAddress.getName()),
				() -> assertThat(expectedAddress.getMobile()).isEqualTo(actualAddress.getMobile()));
	}

	@Test
	@Order(3)
	@DisplayName("Find all addresses by userId test")
	public void givenAddressList_whenFindAllByUserId_thenReturnAddressesOfUser() {
		// given
		IntStream.range(0, 3).mapToLong(Long::valueOf).forEach((e) -> {
			Address address = new Address();
			address.setUser(users.get(0));
			address.setTitle("Home Address");
			address.setName(faker.address().firstName());
			address.setMobile(faker.phoneNumber().phoneNumber());
			address.setState(faker.address().state());
			address.setCity(faker.address().city());
			address.setPincode(faker.address().zipCode());
			address.setLine1(faker.address().fullAddress());
			address.setLine2(faker.address().fullAddress());
			address.setLandmark(faker.address().streetName());
			address.setDefault(true);
			addressRepository.save(address);
		});

		IntStream.range(0, 3).mapToLong(Long::valueOf).forEach((e) -> {
			Address address = new Address();
			address.setUser(users.get(1));
			address.setTitle("Home Address");
			address.setName(faker.address().firstName());
			address.setMobile(faker.phoneNumber().phoneNumber());
			address.setState(faker.address().state());
			address.setCity(faker.address().city());
			address.setPincode(faker.address().zipCode());
			address.setLine1(faker.address().fullAddress());
			address.setLine2(faker.address().fullAddress());
			address.setLandmark(faker.address().streetName());
			address.setDefault(true);
			addressRepository.save(address);
		});

		// when
		List<Address> expectedAddresses = addressRepository.findAllByUserId(users.get(0).getId());

		// then
		assertThat(expectedAddresses.size()).isEqualTo(3);
	}

	@Test
	@Order(4)
	@DisplayName("Find address by Id and userId test")
	public void givenAddressList_whenFindByIdAndUserId_thenReturnAddressOfUser() {
		// given
		List<Address> actualAddresses = new LinkedList<Address>();

		IntStream.range(0, 3).mapToLong(Long::valueOf).forEach((e) -> {
			Address address = new Address();
			address.setUser(users.get(0));
			address.setTitle("Home Address");
			address.setName(faker.address().firstName());
			address.setMobile(faker.phoneNumber().phoneNumber());
			address.setState(faker.address().state());
			address.setCity(faker.address().city());
			address.setPincode(faker.address().zipCode());
			address.setLine1(faker.address().fullAddress());
			address.setLine2(faker.address().fullAddress());
			address.setLandmark(faker.address().streetName());
			address.setDefault(true);
			actualAddresses.add(addressRepository.save(address));
		});

		IntStream.range(0, 3).mapToLong(Long::valueOf).forEach((e) -> {
			Address address = new Address();
			address.setUser(users.get(1));
			address.setTitle("Home Address");
			address.setName(faker.address().firstName());
			address.setMobile(faker.phoneNumber().phoneNumber());
			address.setState(faker.address().state());
			address.setCity(faker.address().city());
			address.setPincode(faker.address().zipCode());
			address.setLine1(faker.address().fullAddress());
			address.setLine2(faker.address().fullAddress());
			address.setLandmark(faker.address().streetName());
			address.setDefault(true);
			actualAddresses.add(addressRepository.save(address));
		});

		// when
		Optional<Address> expectedAddress = addressRepository.findByIdAndUserId(actualAddresses.get(0).getId(), users.get(0).getId());

		// then
		assertAll(() -> assertThat(expectedAddress).isNotEmpty(),
				() -> assertThat(expectedAddress.get().getId()).isEqualTo(actualAddresses.get(0).getId()));
	}

	@Test
	@Order(5)
	@DisplayName("Delete address test")
	public void givenAddress_whenDelete_thenReturnRemoveAddress() {
		// given
		Address address = new Address();
		address.setUser(users.get(0));
		address.setTitle("Home Address");
		address.setName(faker.address().firstName());
		address.setMobile(faker.phoneNumber().phoneNumber());
		address.setState(faker.address().state());
		address.setCity(faker.address().city());
		address.setPincode(faker.address().zipCode());
		address.setLine1(faker.address().fullAddress());
		address.setLine2(faker.address().fullAddress());
		address.setLandmark(faker.address().streetName());
		address.setDefault(true);
		Address actualAddress = addressRepository.save(address);

		// when
		addressRepository.delete(actualAddress);
		Optional<Address> expectedAddress = addressRepository.findById(actualAddress.getId());

		// then
		assertThat(expectedAddress).isEmpty();
	}

}
