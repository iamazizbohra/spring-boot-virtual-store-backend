package com.coedmaster.vstore.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.ArrayList;
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
import org.springframework.data.domain.Sort;

import com.coedmaster.vstore.enums.Gender;
import com.coedmaster.vstore.enums.UserType;
import com.coedmaster.vstore.model.Banner;
import com.coedmaster.vstore.model.Category;
import com.coedmaster.vstore.model.Role;
import com.coedmaster.vstore.model.Store;
import com.coedmaster.vstore.model.User;
import com.coedmaster.vstore.model.embeddable.FullName;
import com.coedmaster.vstore.respository.CategoryRepository;
import com.coedmaster.vstore.respository.RoleRepository;
import com.coedmaster.vstore.respository.StoreRepository;
import com.coedmaster.vstore.respository.UserRepository;
import com.github.javafaker.Faker;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CategoryRepositoryTests {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private StoreRepository storeRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	private final Faker faker = new Faker();

	private Role role;

	private List<User> users = new LinkedList<User>();

	private List<Store> stores = new LinkedList<Store>();

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

			Store store = new Store();
			store.setUser(user);
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
	@DisplayName("Save category test")
	public void givenCategory_whenSave_thenReturnSavedCategory() {
		// given
		Category category = new Category();
		category.setStore(stores.get(0));
		category.setName(faker.name().firstName());
		category.setImage(faker.avatar().image());
		category.setEnabled(true);

		// when
		Category expectedCategory = categoryRepository.save(category);

		// then
		assertAll(() -> assertThat(expectedCategory).isNotNull(),
				() -> assertThat(expectedCategory.getId()).isGreaterThan(0));
	}

	@Test
	@Order(2)
	@DisplayName("Update category test")
	public void givenCategory_whenSave_thenReturnUpdatedCategory() {
		// given
		Category category = new Category();
		category.setStore(stores.get(0));
		category.setName(faker.name().firstName());
		category.setImage(faker.avatar().image());
		category.setEnabled(true);
		Category actualCategory = categoryRepository.save(category);

		actualCategory.setName(faker.name().firstName());
		actualCategory.setImage(faker.avatar().image());
		actualCategory.setEnabled(false);

		// when
		Category expectedCategory = categoryRepository.save(actualCategory);

		// then
		assertAll(() -> assertThat(expectedCategory).isNotNull(),
				() -> assertThat(expectedCategory.getName()).isEqualTo(actualCategory.getName()),
				() -> assertThat(expectedCategory.getImage()).isEqualTo(actualCategory.getImage()),
				() -> assertThat(expectedCategory.isEnabled()).isEqualTo(actualCategory.isEnabled()));
	}

	@Test
	@Order(3)
	@DisplayName("Find all by storeId test")
	public void givenCategoryList_whenFindAllByStoreId_thenReturnCategoriesOfStore() {
		// given
		Category category1 = new Category();
		category1.setStore(stores.get(0));
		category1.setName(faker.name().firstName());
		category1.setImage(faker.avatar().image());
		category1.setEnabled(true);
		categoryRepository.save(category1);

		IntStream.range(0, 3).mapToLong(Long::valueOf).forEach((e) -> {
			Category category = new Category();
			category.setStore(stores.get(1));
			category.setName(faker.name().firstName());
			category.setImage(faker.avatar().image());
			category.setEnabled(e == 0 ? false : true);
			categoryRepository.save(category);
		});

		// when
		List<Category> expectedCategories = categoryRepository.findAllByStoreId(stores.get(1).getId(), Sort.by("name"));

		// then
		assertThat(expectedCategories.size()).isEqualTo(3);
	}

	@Test
	@Order(4)
	@DisplayName("Find all by storeId and enabled test")
	public void givenCategoryList_whenFindAllByStoreIdAndEnabled_thenReturnEnabledCategoriesOfStore() {
		// given
		Category category1 = new Category();
		category1.setStore(stores.get(0));
		category1.setName(faker.name().firstName());
		category1.setImage(faker.avatar().image());
		category1.setEnabled(true);
		categoryRepository.save(category1);

		IntStream.range(0, 3).mapToLong(Long::valueOf).forEach((e) -> {
			Category category = new Category();
			category.setStore(stores.get(1));
			category.setName(faker.name().firstName());
			category.setImage(faker.avatar().image());
			category.setEnabled(e == 0 ? false : true);
			categoryRepository.save(category);
		});

		// when
		List<Category> expectedCategories = categoryRepository.findAllByStoreIdAndEnabled(stores.get(1).getId(), true,
				Sort.by("name"));

		// then
		assertThat(expectedCategories.size()).isEqualTo(2);
	}

	@Test
	@Order(5)
	@DisplayName("Find by Id and storeId test")
	public void givenCategoryList_whenFindByIdAndStoreId_thenReturnCategoryOfStore() {
		// given
		Category category1 = new Category();
		category1.setStore(stores.get(0));
		category1.setName(faker.name().firstName());
		category1.setImage(faker.avatar().image());
		category1.setEnabled(true);
		Category actualCategory = categoryRepository.save(category1);

		IntStream.range(0, 3).mapToLong(Long::valueOf).forEach((e) -> {
			Category category = new Category();
			category.setStore(stores.get(1));
			category.setName(faker.name().firstName());
			category.setImage(faker.avatar().image());
			category.setEnabled(e == 0 ? false : true);
			categoryRepository.save(category);
		});

		// when
		Optional<Category> expectedCategory = categoryRepository.findByIdAndStoreId(actualCategory.getId(),
				stores.get(0).getId());

		// then
		assertAll(() -> assertThat(expectedCategory).isNotEmpty(),
				() -> assertThat(expectedCategory.get().getId()).isEqualTo(actualCategory.getId()));
	}

	@Test
	@Order(6)
	@DisplayName("Find all by Id In and storeId test")
	public void givenCategoryList_whenFindAllByIdInAndStoreId_thenReturnCategoriesOfStore() {
		// given
		Category category1 = new Category();
		category1.setStore(stores.get(0));
		category1.setName(faker.name().firstName());
		category1.setImage(faker.avatar().image());
		category1.setEnabled(true);
		categoryRepository.save(category1);

		List<Long> categoryIds = new ArrayList<Long>();
		IntStream.range(0, 3).mapToLong(Long::valueOf).forEach((e) -> {
			Category category = new Category();
			category.setStore(stores.get(1));
			category.setName(faker.name().firstName());
			category.setImage(faker.avatar().image());
			category.setEnabled(e == 0 ? false : true);
			category = categoryRepository.save(category);

			categoryIds.add(category.getId());
		});

		// when
		List<Category> expectedCategories = categoryRepository.findAllByIdInAndStoreId(categoryIds,
				stores.get(1).getId(), Sort.by("name"));

		// then
		assertThat(expectedCategories.size()).isEqualTo(3);
	}

	@Test
	@Order(7)
	@DisplayName("Find all by Id In and storeId and enabled test")
	public void givenCategoryList_whenFindAllByIdInAndStoreIdAndEnabled_thenReturnEnabledCategoriesOfStore() {
		// given
		Category category1 = new Category();
		category1.setStore(stores.get(0));
		category1.setName(faker.name().firstName());
		category1.setImage(faker.avatar().image());
		category1.setEnabled(true);
		categoryRepository.save(category1);

		List<Long> categoryIds = new ArrayList<Long>();
		IntStream.range(0, 3).mapToLong(Long::valueOf).forEach((e) -> {
			Category category = new Category();
			category.setStore(stores.get(1));
			category.setName(faker.name().firstName());
			category.setImage(faker.avatar().image());
			category.setEnabled(e == 0 ? false : true);
			category = categoryRepository.save(category);

			categoryIds.add(category.getId());
		});

		// when
		List<Category> expectedCategories = categoryRepository.findAllByIdInAndStoreIdAndEnabled(categoryIds,
				stores.get(1).getId(), true, Sort.by("name"));

		// then
		assertThat(expectedCategories.size()).isEqualTo(2);
	}

	@Test
	@Order(8)
	@DisplayName("Delete category test")
	public void givenCategory_whenDelete_thenRemoveCategory() {
		// given
		Category category = new Category();
		category.setStore(stores.get(0));
		category.setName(faker.name().firstName());
		category.setImage(faker.avatar().image());
		category.setEnabled(true);
		Category actualCategory = categoryRepository.save(category);

		// When
		categoryRepository.delete(actualCategory);
		Optional<Category> expectedCategory = categoryRepository.findById(actualCategory.getId());

		// then
		assertThat(expectedCategory).isEmpty();
	}

}
