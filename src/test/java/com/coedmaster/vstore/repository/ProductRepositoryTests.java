package com.coedmaster.vstore.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
import org.springframework.data.domain.Sort;

import com.coedmaster.vstore.enums.Gender;
import com.coedmaster.vstore.enums.UserType;
import com.coedmaster.vstore.model.Category;
import com.coedmaster.vstore.model.Product;
import com.coedmaster.vstore.model.Role;
import com.coedmaster.vstore.model.Store;
import com.coedmaster.vstore.model.User;
import com.coedmaster.vstore.model.embeddable.FullName;
import com.coedmaster.vstore.respository.CategoryRepository;
import com.coedmaster.vstore.respository.ProductRepository;
import com.coedmaster.vstore.respository.RoleRepository;
import com.coedmaster.vstore.respository.StoreRepository;
import com.coedmaster.vstore.respository.UserRepository;
import com.github.javafaker.Faker;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductRepositoryTests {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private StoreRepository storeRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ProductRepository productRepository;

	private final Faker faker = new Faker();

	private Role role;

	private List<User> users = new LinkedList<User>();

	private List<Store> stores = new LinkedList<Store>();

	private Map<Store, LinkedList<Category>> storeCategories = new HashMap<Store, LinkedList<Category>>();

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

			storeCategories.put(stores.get((int) e), new LinkedList<Category>());

			IntStream.range(0, 2).mapToLong(Long::valueOf).forEach((e1) -> {
				Category category = new Category();
				category.setStore(stores.get((int) e));
				category.setName(faker.name().firstName());
				category.setImage(faker.avatar().image());
				category.setEnabled(e1 == 0 ? false : true);
				category = categoryRepository.save(category);

				storeCategories.get(stores.get((int) e)).add(category);

				Product product = new Product();
				product.setStore(stores.get((int) e));
				product.setCategory(category);
				product.setName(faker.commerce().productName());
				product.setDescription(faker.lorem().sentence());
				product.setImage(faker.avatar().image());
				product.setPrice(100);
				product.setOldPrice(80);
				product.setQuantity(10);
				product.setEnabled(true);
				productRepository.save(product);
			});
		});

	}

	@Test
	@Order(1)
	@DisplayName("Save product test")
	public void givenProduct_whenSave_thenReturnSavedProduct() {
		// given
		Store store = stores.get(0);
		Category category = storeCategories.get(store).get(0);

		Product product = new Product();
		product.setStore(store);
		product.setCategory(category);
		product.setName(faker.commerce().productName());
		product.setDescription(faker.lorem().sentence());
		product.setImage(faker.avatar().image());
		product.setPrice(100);
		product.setOldPrice(80);
		product.setQuantity(10);
		product.setEnabled(true);

		// when
		Product expectedProduct = productRepository.save(product);

		// then
		assertAll(() -> assertThat(expectedProduct).isNotNull(),
				() -> assertThat(expectedProduct.getId()).isGreaterThan(0));
	}

	@Test
	@Order(2)
	@DisplayName("Update product test")
	public void givenProduct_whenUpdate_thenReturnUpdatedProduct() {
		// given
		Store store = stores.get(0);
		Category category = storeCategories.get(store).get(0);

		Product product = new Product();
		product.setStore(store);
		product.setCategory(category);
		product.setName(faker.commerce().productName());
		product.setDescription(faker.lorem().sentence());
		product.setImage(faker.avatar().image());
		product.setPrice(100);
		product.setOldPrice(80);
		product.setQuantity(10);
		product.setEnabled(true);
		Product actualProduct = productRepository.save(product);

		// when
		category = storeCategories.get(store).get(1);
		actualProduct.setCategory(category);
		actualProduct.setName(faker.commerce().productName());
		actualProduct.setDescription(faker.lorem().sentence());
		actualProduct.setImage(faker.avatar().image());
		actualProduct.setPrice(100);
		actualProduct.setOldPrice(80);
		actualProduct.setQuantity(10);
		actualProduct.setEnabled(false);
		Product expectedProduct = productRepository.save(actualProduct);

		// then
		assertAll(() -> assertThat(expectedProduct).isNotNull(),
				() -> assertThat(expectedProduct.getCategory().getName())
						.isEqualTo(actualProduct.getCategory().getName()),
				() -> assertThat(expectedProduct.getName()).isEqualTo(actualProduct.getName()),
				() -> assertThat(expectedProduct.getImage()).isEqualTo(actualProduct.getImage()),
				() -> assertThat(expectedProduct.isEnabled()).isEqualTo(actualProduct.isEnabled()));
	}

	@Test
	@Order(3)
	@DisplayName("Find all products by storeId test")
	public void givenProductList_whenFindAllByStoreId_thenReturnProductsOfStore() {
		// given
		Store store = stores.get(0);
		Category category = storeCategories.get(store).get(0);

		IntStream.range(0, 3).mapToLong(Long::valueOf).forEach((e) -> {
			Product product = new Product();
			product.setStore(store);
			product.setCategory(category);
			product.setName(faker.commerce().productName());
			product.setDescription(faker.lorem().sentence());
			product.setImage(faker.avatar().image());
			product.setPrice(100);
			product.setOldPrice(80);
			product.setQuantity(10);
			product.setEnabled(e == 0 ? false : true);
			productRepository.save(product);
		});

		// when
		PageRequest pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.valueOf("DESC"), "name"));
		Page<Product> productPage = productRepository.findAllByStoreId(store.getId(), pageable);

		// then
		assertThat(productPage.getContent().size()).isEqualTo(5);
	}

	@Test
	@Order(4)
	@DisplayName("Find all products by storeId and enabled test")
	public void givenProductList_whenFindAllByStoreIdAndEnabled_thenReturnProductsOfStore() {
		// given
		Store store = stores.get(0);
		Category category = storeCategories.get(store).get(0);

		IntStream.range(0, 3).mapToLong(Long::valueOf).forEach((e) -> {
			Product product = new Product();
			product.setStore(store);
			product.setCategory(category);
			product.setName(faker.commerce().productName());
			product.setDescription(faker.lorem().sentence());
			product.setImage(faker.avatar().image());
			product.setPrice(100);
			product.setOldPrice(80);
			product.setQuantity(10);
			product.setEnabled(e == 0 ? false : true);
			productRepository.save(product);
		});

		// when
		PageRequest pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.valueOf("DESC"), "name"));
		Page<Product> productPage = productRepository.findAllByStoreIdAndEnabled(store.getId(), true, pageable);

		// then
		assertThat(productPage.getContent().size()).isEqualTo(4);
	}

	@Test
	@Order(5)
	@DisplayName("Find product by Id and storeId test")
	public void givenProductList_whenFindByIdAndStoreId_thenReturnProductOfStore() {
		// given
		Store store = stores.get(0);
		Category category = storeCategories.get(store).get(0);
		List<Product> actualProducts = new LinkedList<Product>();

		IntStream.range(0, 3).mapToLong(Long::valueOf).forEach((e) -> {
			Product product = new Product();
			product.setStore(store);
			product.setCategory(category);
			product.setName(faker.commerce().productName());
			product.setDescription(faker.lorem().sentence());
			product.setImage(faker.avatar().image());
			product.setPrice(100);
			product.setOldPrice(80);
			product.setQuantity(10);
			product.setEnabled(e == 0 ? false : true);
			actualProducts.add(productRepository.save(product));
		});

		// when
		Optional<Product> expectedProduct = productRepository.findByIdAndStoreId(actualProducts.get(0).getId(),
				store.getId());

		// then
		assertAll(() -> assertThat(expectedProduct).isNotEmpty(),
				() -> assertThat(expectedProduct.get().getId()).isEqualTo(actualProducts.get(0).getId()));
	}

	@Test
	@Order(6)
	@DisplayName("Find product by Id and storeId and enabled test")
	public void givenProductList_whenFindByIdAndStoreIdAndEnabled_thenReturnEnabledProductOfStore() {
		// given
		Store store = stores.get(0);
		Category category = storeCategories.get(store).get(0);
		List<Product> actualProducts = new LinkedList<Product>();

		IntStream.range(0, 3).mapToLong(Long::valueOf).forEach((e) -> {
			Product product = new Product();
			product.setStore(store);
			product.setCategory(category);
			product.setName(faker.commerce().productName());
			product.setDescription(faker.lorem().sentence());
			product.setImage(faker.avatar().image());
			product.setPrice(100);
			product.setOldPrice(80);
			product.setQuantity(10);
			product.setEnabled(e == 0 ? false : true);
			actualProducts.add(productRepository.save(product));
		});

		// when
		Optional<Product> expectedProduct = productRepository
				.findByIdAndStoreIdAndEnabled(actualProducts.get(1).getId(), store.getId(), true);

		// then
		assertAll(() -> assertThat(expectedProduct).isNotEmpty(),
				() -> assertThat(expectedProduct.get().getId()).isEqualTo(actualProducts.get(1).getId()),
				() -> assertThat(expectedProduct.get().isEnabled()).isEqualTo(actualProducts.get(1).isEnabled()));
	}

	@Test
	@Order(7)
	@DisplayName("Find all products by storeId and categoryId In test")
	public void givenProductList_whenFindAllByStoreIdAndCategoryIdIn_thenReturnProductsOfStore() {
		// given
		Store store = stores.get(0);
		Category category = storeCategories.get(store).get(0);

		IntStream.range(0, 3).mapToLong(Long::valueOf).forEach((e) -> {
			Product product = new Product();
			product.setStore(store);
			product.setCategory(category);
			product.setName(faker.commerce().productName());
			product.setDescription(faker.lorem().sentence());
			product.setImage(faker.avatar().image());
			product.setPrice(100);
			product.setOldPrice(80);
			product.setQuantity(10);
			product.setEnabled(e == 0 ? false : true);
			productRepository.save(product);
		});

		// when
		PageRequest pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.valueOf("DESC"), "name"));
		Page<Product> expectedProduct = productRepository.findAllByStoreIdAndCategoryIdIn(store.getId(),
				List.of(category.getId()), pageable);

		// then
		assertThat(expectedProduct.getContent().size()).isEqualTo(4);
	}

	@Test
	@Order(8)
	@DisplayName("Find all products by storeId and categoryId and enabled In test")
	public void givenProductList_whenFindAllByStoreIdAndCategoryIdInAndEnabled_thenReturnEnabledProductsOfStore() {
		// given
		Store store = stores.get(0);
		Category category = storeCategories.get(store).get(0);

		IntStream.range(0, 3).mapToLong(Long::valueOf).forEach((e) -> {
			Product product = new Product();
			product.setStore(store);
			product.setCategory(category);
			product.setName(faker.commerce().productName());
			product.setDescription(faker.lorem().sentence());
			product.setImage(faker.avatar().image());
			product.setPrice(100);
			product.setOldPrice(80);
			product.setQuantity(10);
			product.setEnabled(e == 0 ? false : true);
			productRepository.save(product);
		});

		// when
		PageRequest pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.valueOf("DESC"), "name"));
		Page<Product> expectedProduct = productRepository.findAllByStoreIdAndCategoryIdInAndEnabled(store.getId(),
				List.of(category.getId()), true, pageable);

		// then
		assertThat(expectedProduct.getContent().size()).isEqualTo(3);
	}

	@Test
	@Order(9)
	@DisplayName("Delete product test")
	public void givenProduct_whenDelete_thenRemoveProduct() {
		// given
		Store store = stores.get(0);
		Category category = storeCategories.get(store).get(0);

		Product product = new Product();
		product.setStore(store);
		product.setCategory(category);
		product.setName(faker.commerce().productName());
		product.setDescription(faker.lorem().sentence());
		product.setImage(faker.avatar().image());
		product.setPrice(100);
		product.setOldPrice(80);
		product.setQuantity(10);
		product.setEnabled(true);
		Product actualProduct = productRepository.save(product);

		// When
		productRepository.delete(actualProduct);
		Optional<Product> expectedProduct = productRepository.findById(actualProduct.getId());

		// then
		assertThat(expectedProduct).isEmpty();
	}

}
