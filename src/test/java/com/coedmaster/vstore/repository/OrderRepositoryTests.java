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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.coedmaster.vstore.enums.Gender;
import com.coedmaster.vstore.enums.OrderStatus;
import com.coedmaster.vstore.enums.UserType;
import com.coedmaster.vstore.model.Address;
import com.coedmaster.vstore.model.Role;
import com.coedmaster.vstore.model.Store;
import com.coedmaster.vstore.model.User;
import com.coedmaster.vstore.model.embeddable.FullName;
import com.coedmaster.vstore.model.specification.OrderSpecs;
import com.coedmaster.vstore.respository.AddressRepository;
import com.coedmaster.vstore.respository.OrderRepository;
import com.coedmaster.vstore.respository.RoleRepository;
import com.coedmaster.vstore.respository.StoreRepository;
import com.coedmaster.vstore.respository.UserRepository;
import com.github.javafaker.Faker;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderRepositoryTests {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private StoreRepository storeRepository;

	@Autowired
	private OrderRepository orderRepository;

	private final Faker faker = new Faker();

	private Role role;

	private List<User> users = new LinkedList<User>();

	private List<Address> addresses = new LinkedList<Address>();

	private List<Store> stores = new LinkedList<Store>();

	private List<com.coedmaster.vstore.model.Order> orders = new LinkedList<com.coedmaster.vstore.model.Order>();

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

		IntStream.range(0, 2).mapToLong(Long::valueOf).forEach((e) -> {
			IntStream.range(0, 2).mapToLong(Long::valueOf).forEach((e1) -> {
				User user = users.get((int) e);
				Address shippingAddress = addresses.get((int) e);
				Store store = stores.get((int) e);

				com.coedmaster.vstore.model.Order order = new com.coedmaster.vstore.model.Order();
				order.setUser(user);
				order.setStore(store);
				order.setName(shippingAddress.getName());
				order.setMobile(shippingAddress.getMobile());
				order.setEmail(user.getEmail());
				order.setState(shippingAddress.getState());
				order.setCity(shippingAddress.getCity());
				order.setPincode(shippingAddress.getPincode());
				order.setLine1(shippingAddress.getLine1());
				order.setLine2(shippingAddress.getLine2());
				order.setLandmark(shippingAddress.getLandmark());
				order.setSubTotal(100);
				order.setShippingCharges(0);
				order.setTotal(100);
				order.setStatus(OrderStatus.PENDING);
				orders.add(orderRepository.save(order));
			});
		});

	}

	@Test
	@Order(1)
	@DisplayName("Save order test")
	public void givenOrder_whenSave_thenReturnSavedOrder() {
		// given
		User user = users.get(0);
		Address shippingAddress = addresses.get(0);
		Store store = stores.get(0);

		com.coedmaster.vstore.model.Order order = new com.coedmaster.vstore.model.Order();
		order.setUser(user);
		order.setStore(store);
		order.setName(shippingAddress.getName());
		order.setMobile(shippingAddress.getMobile());
		order.setEmail(user.getEmail());
		order.setState(shippingAddress.getState());
		order.setCity(shippingAddress.getCity());
		order.setPincode(shippingAddress.getPincode());
		order.setLine1(shippingAddress.getLine1());
		order.setLine2(shippingAddress.getLine2());
		order.setLandmark(shippingAddress.getLandmark());
		order.setSubTotal(100);
		order.setShippingCharges(0);
		order.setTotal(100);
		order.setStatus(OrderStatus.PENDING);

		// when
		com.coedmaster.vstore.model.Order expectedOrder = orderRepository.save(order);

		// then
		assertAll(() -> assertThat(expectedOrder).isNotNull(),
				() -> assertThat(expectedOrder.getId()).isGreaterThan(0));
	}

	@Test
	@Order(2)
	@DisplayName("Find all orders by userId test")
	public void givenOrderList_whenFindAllByUserId_thenReturnOrdersOfUser() {
		// given

		// when
		Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.valueOf("DESC"), "id"));
		Page<com.coedmaster.vstore.model.Order> orderPage = orderRepository.findAllByUserId(users.get(0).getId(),
				pageable);

		// then
		assertThat(orderPage.getContent().size()).isEqualTo(2);
	}

	@Test
	@Order(3)
	@DisplayName("Find all orders by storeId test")
	public void givenOrderList_whenFindAllByStoreId_thenReturnOrdersOfStore() {
		// given

		// when
		Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.valueOf("DESC"), "id"));
		Page<com.coedmaster.vstore.model.Order> orderPage = orderRepository.findAllByStoreId(stores.get(0).getId(),
				pageable);

		// then
		assertThat(orderPage.getContent().size()).isEqualTo(2);
	}

	@Test
	@Order(4)
	@DisplayName("Find all orders by storeId and status test")
	public void givenOrderList_whenFindAllByStoreIdAndStatus_thenReturnOrdersOfStore() {
		// given

		// when
		Specification<com.coedmaster.vstore.model.Order> specs = Specification
				.where(OrderSpecs.hasStoreId(stores.get(0).getId()));
		specs = specs.and(OrderSpecs.hasStatus(OrderStatus.PENDING));
		Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.valueOf("DESC"), "id"));
		Page<com.coedmaster.vstore.model.Order> orderPage = orderRepository.findAll(specs, pageable);

		// then
		assertThat(orderPage.getContent().size()).isEqualTo(2);
	}

	@Test
	@Order(5)
	@DisplayName("Find order by Id and userId test")
	public void givenOrderList_whenFindByIdAndUserId_thenReturnOrderOfUser() {
		// given

		// when
		Optional<com.coedmaster.vstore.model.Order> expectedOrder = orderRepository
				.findByIdAndUserId(orders.get(0).getId(), users.get(0).getId());

		// then
		assertAll(() -> assertThat(expectedOrder).isNotEmpty(),
				() -> assertThat(expectedOrder.get().getId()).isEqualTo(orders.get(0).getId()));
	}

	@Test
	@Order(6)
	@DisplayName("Find order by Id and storeId test")
	public void givenOrderList_whenFindByIdAndStoreId_thenReturnOrderOfStore() {
		// given

		// when
		Optional<com.coedmaster.vstore.model.Order> expectedOrder = orderRepository
				.findByIdAndStoreId(orders.get(0).getId(), stores.get(0).getId());

		// then
		assertAll(() -> assertThat(expectedOrder).isNotEmpty(),
				() -> assertThat(expectedOrder.get().getId()).isEqualTo(orders.get(0).getId()));
	}

}
