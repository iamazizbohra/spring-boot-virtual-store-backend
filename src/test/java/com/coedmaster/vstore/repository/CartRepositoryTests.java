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
import com.coedmaster.vstore.model.Cart;
import com.coedmaster.vstore.model.Role;
import com.coedmaster.vstore.model.Store;
import com.coedmaster.vstore.model.User;
import com.coedmaster.vstore.model.embeddable.FullName;
import com.coedmaster.vstore.respository.AddressRepository;
import com.coedmaster.vstore.respository.CartRepository;
import com.coedmaster.vstore.respository.RoleRepository;
import com.coedmaster.vstore.respository.StoreRepository;
import com.coedmaster.vstore.respository.UserRepository;
import com.github.javafaker.Faker;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CartRepositoryTests {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private StoreRepository storeRepository;

	@Autowired
	private CartRepository cartRepository;

	private final Faker faker = new Faker();

	private Role role;

	private List<User> users = new LinkedList<User>();

	private List<Address> addresses = new LinkedList<Address>();

	private List<Store> stores = new LinkedList<Store>();

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

			Address address = new Address();
			address.setUser(users.get((int) e));
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
			addresses.add(addressRepository.save(address));

			Store store = new Store();
			store.setUser(users.get((int) e));
			store.setName("Store " + e);
			store.setCode("store" + e);
			store.setMobile(faker.phoneNumber().phoneNumber());
			store.setWhatsapp(faker.phoneNumber().phoneNumber());
			store.setEmail(faker.internet().emailAddress());
			store.setLatitude(faker.address().latitude());
			store.setLongitude(faker.address().longitude());
			store.setAddress(faker.address().fullAddress());
			store.setEnabled(true);
			stores.add(storeRepository.save(store));
		});

	}

	@Test
	@Order(1)
	@DisplayName("Save cart test")
	public void givenCart_whenSave_thenReturnSavedCart() {
		// given
		Cart cart = new Cart();
		cart.setUser(users.get(0));
		cart.setStore(stores.get(0));
		cart.setShippingAddress(addresses.get(0));

		// when
		Cart expectedCart = cartRepository.save(cart);

		// then
		assertAll(() -> assertThat(expectedCart).isNotNull(), () -> assertThat(expectedCart.getId()).isGreaterThan(0));
	}

	@Test
	@Order(2)
	@DisplayName("Find by userId test")
	public void givenCartList_whenFindByUserId_thenReturnUserCart() {
		// given
		List<Cart> actualCarts = new LinkedList<Cart>();
		IntStream.range(0, 2).mapToLong(Long::valueOf).forEach((e) -> {
			Cart cart = new Cart();
			cart.setUser(users.get((int) e));
			cart.setStore(stores.get((int) e));
			cart.setShippingAddress(addresses.get((int) e));
			actualCarts.add(cartRepository.save(cart));
		});

		// when
		Optional<Cart> expectedCart = cartRepository.findByUserId(users.get(0).getId());

		// then
		assertAll(() -> assertThat(expectedCart).isNotEmpty(),
				() -> assertThat(expectedCart.get().getId()).isEqualTo(actualCarts.get(0).getId()));
	}

	@Test
	@Order(3)
	@DisplayName("Find by userId and storeId test")
	public void givenCartList_whenFindByUserIdAndStoreId_thenReturnUserCart() {
		// given
		List<Cart> actualCarts = new LinkedList<Cart>();
		IntStream.range(0, 2).mapToLong(Long::valueOf).forEach((e) -> {
			Cart cart = new Cart();
			cart.setUser(users.get((int) e));
			cart.setStore(stores.get((int) e));
			cart.setShippingAddress(addresses.get((int) e));
			actualCarts.add(cartRepository.save(cart));
		});

		// when
		Optional<Cart> expectedCart = cartRepository.findByUserIdAndStoreId(users.get(0).getId(),
				stores.get(0).getId());

		// then
		assertAll(() -> assertThat(expectedCart).isNotEmpty(),
				() -> assertThat(expectedCart.get().getId()).isEqualTo(actualCarts.get(0).getId()));
	}

	@Test
	@Order(4)
	@DisplayName("Delete cart test")
	public void givenCart_whenDelete_thenRemoveCart() {
		// given
		Cart cart = new Cart();
		cart.setUser(users.get(0));
		cart.setStore(stores.get(0));
		cart.setShippingAddress(addresses.get(0));
		Cart actualCart = cartRepository.save(cart);

		// When
		cartRepository.delete(actualCart);
		Optional<Cart> expectedCart = cartRepository.findById(actualCart.getId());

		// then
		assertThat(expectedCart).isEmpty();
	}

}
