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
import com.coedmaster.vstore.enums.UserType;
import com.coedmaster.vstore.model.Role;
import com.coedmaster.vstore.model.Store;
import com.coedmaster.vstore.model.User;
import com.coedmaster.vstore.model.embeddable.FullName;
import com.coedmaster.vstore.model.specification.StoreSpecs;
import com.coedmaster.vstore.respository.RoleRepository;
import com.coedmaster.vstore.respository.StoreRepository;
import com.coedmaster.vstore.respository.UserRepository;
import com.github.javafaker.Faker;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StoreRepositoryTests {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private StoreRepository storeRepository;

	private final Faker faker = new Faker();

	private Role role;

	private List<User> users = new LinkedList<User>();

	private List<Store> stores = new LinkedList<Store>();

	@BeforeEach
	public void beforeEach() {
		role = new Role();
		role.setName("ROLE_SELLER");
		role = roleRepository.save(role);

		IntStream.range(0, 3).mapToLong(Long::valueOf).forEach((e) -> {
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
	@DisplayName("Save store test")
	public void givenStore_whenSave_thenReturnSavedStore() {
		// given
		Store store = new Store();
		store.setUser(users.get(0));
		store.setName("Store 1");
		store.setCode("store1");
		store.setMobile(faker.phoneNumber().phoneNumber());
		store.setWhatsapp(faker.phoneNumber().phoneNumber());
		store.setEmail(faker.internet().emailAddress());
		store.setLatitude(faker.address().latitude());
		store.setLongitude(faker.address().longitude());
		store.setAddress(faker.address().fullAddress());
		store.setEnabled(true);

		// when
		Store expectedStore = storeRepository.save(store);

		// then
		assertAll(() -> assertThat(expectedStore).isNotNull(),
				() -> assertThat(expectedStore.getId()).isGreaterThan(0));
	}

	@Test
	@Order(2)
	@DisplayName("Update store test")
	public void givenStore_whenUpdate_thenReturnStore() {
		// given
		Store store = new Store();
		store.setUser(users.get(0));
		store.setName("Store 1");
		store.setCode("store1");
		store.setMobile(faker.phoneNumber().phoneNumber());
		store.setWhatsapp(faker.phoneNumber().phoneNumber());
		store.setEmail(faker.internet().emailAddress());
		store.setLatitude(faker.address().latitude());
		store.setLongitude(faker.address().longitude());
		store.setAddress(faker.address().fullAddress());
		store.setEnabled(true);
		Store actualStore = storeRepository.save(store);

		// when
		actualStore.setName("Store 2");
		actualStore.setCode("store2");
		actualStore.setMobile(faker.phoneNumber().phoneNumber());
		actualStore.setWhatsapp(faker.phoneNumber().phoneNumber());
		actualStore.setEmail(faker.internet().emailAddress());
		actualStore.setLatitude(faker.address().latitude());
		actualStore.setLongitude(faker.address().longitude());
		actualStore.setAddress(faker.address().fullAddress());
		actualStore.setEnabled(false);
		Store expectedStore = storeRepository.save(actualStore);

		// then
		assertAll(() -> assertThat(expectedStore).isNotNull(),
				() -> assertThat(expectedStore.getName()).isEqualTo(actualStore.getName()),
				() -> assertThat(expectedStore.getCode()).isEqualTo(actualStore.getCode()),
				() -> assertThat(expectedStore.getMobile()).isEqualTo(actualStore.getMobile()),
				() -> assertThat(expectedStore.isEnabled()).isEqualTo(actualStore.isEnabled()));
	}

	@Test
	@Order(3)
	@DisplayName("Find store by Id test")
	public void givenStore_whenFindById_thenReturnStore() {
		// given
		IntStream.range(0, 3).mapToLong(Long::valueOf).forEach((e) -> {
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
			store.setEnabled(e == 0 ? false : true);
			stores.add(storeRepository.save(store));
		});

		// when
		Optional<Store> expectedStore = storeRepository.findById(stores.get(0).getId());

		// then
		assertAll(() -> assertThat(expectedStore).isNotEmpty(),
				() -> assertThat(expectedStore.get().getId()).isEqualTo(stores.get(0).getId()));
	}

	@Test
	@Order(4)
	@DisplayName("Find store by userId test")
	public void givenUser_whenFindByUserId_thenReturnStoreOfUser() {
		// given
		IntStream.range(0, 3).mapToLong(Long::valueOf).forEach((e) -> {
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
			store.setEnabled(e == 0 ? false : true);
			stores.add(storeRepository.save(store));
		});

		// when
		Optional<Store> expectedStore = storeRepository.findByUserId(users.get(0).getId());

		// then
		assertAll(() -> assertThat(expectedStore).isNotEmpty(),
				() -> assertThat(expectedStore.get().getId()).isEqualTo(stores.get(0).getId()));
	}

	@Test
	@Order(5)
	@DisplayName("Find store by code test")
	public void givenStore_whenFindByCode_thenReturnStore() {
		// given
		IntStream.range(0, 3).mapToLong(Long::valueOf).forEach((e) -> {
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
			store.setEnabled(e == 0 ? false : true);
			stores.add(storeRepository.save(store));
		});

		// when
		Optional<Store> expectedStore = storeRepository.findByCode(stores.get(0).getCode());

		// then
		assertAll(() -> assertThat(expectedStore).isNotEmpty(),
				() -> assertThat(expectedStore.get().getId()).isEqualTo(stores.get(0).getId()));
	}

	@Test
	@Order(6)
	@DisplayName("Find all stores by enabled test")
	public void givenStoreList_whenFindAllByEnabled_thenReturnEnabledStores() {
		// given
		IntStream.range(0, 3).mapToLong(Long::valueOf).forEach((e) -> {
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
			store.setEnabled(e == 0 ? false : true);
			stores.add(storeRepository.save(store));
		});

		// when
		Specification<Store> specs = Specification.where(StoreSpecs.isEnabled(true));
		Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));
		Page<Store> storePage = storeRepository.findAll(specs, pageable);

		// then
		assertThat(storePage.getContent().size()).isEqualTo(2);
	}

	@Test
	@Order(7)
	@DisplayName("Find store by code and enabled test")
	public void givenStoreList_whenFindByCodeAndEnabled_thenReturnEnabledStore() {
		// given
		IntStream.range(0, 3).mapToLong(Long::valueOf).forEach((e) -> {
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
			store.setEnabled(e == 0 ? false : true);
			stores.add(storeRepository.save(store));
		});

		// when
		Specification<Store> specs = Specification.where(StoreSpecs.hasCode(stores.get(1).getCode()))
				.and(StoreSpecs.isEnabled(true));
		Optional<Store> expectedStore = storeRepository.findOne(specs);

		// then
		assertAll(() -> assertThat(expectedStore).isNotEmpty(),
				() -> assertThat(expectedStore.get().getId()).isEqualTo(stores.get(1).getId()));
	}

}
