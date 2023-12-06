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

import com.coedmaster.vstore.enums.Gender;
import com.coedmaster.vstore.enums.UserType;
import com.coedmaster.vstore.model.Address;
import com.coedmaster.vstore.model.Cart;
import com.coedmaster.vstore.model.CartItem;
import com.coedmaster.vstore.model.Category;
import com.coedmaster.vstore.model.Product;
import com.coedmaster.vstore.model.Role;
import com.coedmaster.vstore.model.Store;
import com.coedmaster.vstore.model.User;
import com.coedmaster.vstore.model.embeddable.FullName;
import com.coedmaster.vstore.respository.AddressRepository;
import com.coedmaster.vstore.respository.CartItemRepository;
import com.coedmaster.vstore.respository.CartRepository;
import com.coedmaster.vstore.respository.CategoryRepository;
import com.coedmaster.vstore.respository.ProductRepository;
import com.coedmaster.vstore.respository.RoleRepository;
import com.coedmaster.vstore.respository.StoreRepository;
import com.coedmaster.vstore.respository.UserRepository;
import com.github.javafaker.Faker;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CartItemRepositoryTests {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private StoreRepository storeRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private CartItemRepository cartItemRepository;

	private final Faker faker = new Faker();

	private Role role;

	private List<User> users = new LinkedList<User>();

	private List<Address> addresses = new LinkedList<Address>();

	private List<Store> stores = new LinkedList<Store>();

	private List<Cart> carts = new LinkedList<Cart>();

	private Map<Store, LinkedList<Product>> storeProducts = new HashMap<Store, LinkedList<Product>>();

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

			storeProducts.put(stores.get((int) e), new LinkedList<Product>());

			IntStream.range(0, 2).mapToLong(Long::valueOf).forEach((e1) -> {
				Category category = new Category();
				category.setStore(stores.get((int) e));
				category.setName(faker.name().firstName());
				category.setImage(faker.avatar().image());
				category.setEnabled(e1 == 0 ? false : true);
				category = categoryRepository.save(category);

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
				product = productRepository.save(product);

				storeProducts.get(stores.get((int) e)).add(product);
			});

			Cart cart = new Cart();
			cart.setUser(users.get(0));
			cart.setStore(stores.get(0));
			cart.setShippingAddress(addresses.get(0));
			carts.add(cartRepository.save(cart));
		});
	}

	@Test
	@Order(1)
	@DisplayName("Save cartItem test")
	public void givenCartItem_whenSave_thenReturnSavedCartItem() {
		// given
		Store store = stores.get(0);
		Product product = storeProducts.get(store).get(0);

		CartItem cartItem = new CartItem();
		cartItem.setCart(carts.get(0));
		cartItem.setProduct(product);
		cartItem.setName(product.getName());
		cartItem.setImage(product.getImage());
		cartItem.setPrice(product.getPrice());
		cartItem.setOldPrice(product.getOldPrice());
		cartItem.setQuantity(1);

		// when
		CartItem expectedCartItem = cartItemRepository.save(cartItem);

		// then
		assertAll(() -> assertThat(expectedCartItem).isNotNull(),
				() -> assertThat(expectedCartItem.getId()).isGreaterThan(0));
	}

	@Test
	@Order(2)
	@DisplayName("Update cartItem quantity test")
	public void givenCartItem_whenUpdateQuantity_thenReturnUpdatedCartItem() {
		// given
		Store store = stores.get(0);
		Product product = storeProducts.get(store).get(0);

		CartItem cartItem = new CartItem();
		cartItem.setCart(carts.get(0));
		cartItem.setProduct(product);
		cartItem.setName(product.getName());
		cartItem.setImage(product.getImage());
		cartItem.setPrice(product.getPrice());
		cartItem.setOldPrice(product.getOldPrice());
		cartItem.setQuantity(1);
		CartItem actualCartItem = cartItemRepository.save(cartItem);

		// when
		actualCartItem.setQuantity(2);
		CartItem expectedCartItem = cartItemRepository.save(actualCartItem);

		// then
		assertAll(() -> assertThat(expectedCartItem).isNotNull(),
				() -> assertThat(expectedCartItem.getQuantity()).isEqualTo(2));
	}

	@Test
	@Order(3)
	@DisplayName("Find all cartItems by cartId test")
	public void givenCartItemList_whenFindAllByCartId_thenReturnCartItemsOfCart() {
		// given
		IntStream.range(0, 2).mapToLong(Long::valueOf).forEach((e) -> {
			Store store = stores.get(0);
			Product product = storeProducts.get(store).get((int) e);

			CartItem cartItem = new CartItem();
			cartItem.setCart(carts.get(0));
			cartItem.setProduct(product);
			cartItem.setName(product.getName());
			cartItem.setImage(product.getImage());
			cartItem.setPrice(product.getPrice());
			cartItem.setOldPrice(product.getOldPrice());
			cartItem.setQuantity(1);
			cartItemRepository.save(cartItem);
		});

		// when
		List<CartItem> expectedCartItems = cartItemRepository.findAllByCartId(carts.get(0).getId());

		// then
		assertThat(expectedCartItems.size()).isEqualTo(2);
	}

	@Test
	@Order(4)
	@DisplayName("Find cartItem by Id and cartId test")
	public void givenCartItemList_whenFindByIdAndCartId_thenReturnCartItemOfCart() {
		// given
		List<CartItem> actualCartItems = new LinkedList<CartItem>();
		IntStream.range(0, 2).mapToLong(Long::valueOf).forEach((e) -> {
			Store store = stores.get(0);
			Product product = storeProducts.get(store).get((int) e);

			CartItem cartItem = new CartItem();
			cartItem.setCart(carts.get(0));
			cartItem.setProduct(product);
			cartItem.setName(product.getName());
			cartItem.setImage(product.getImage());
			cartItem.setPrice(product.getPrice());
			cartItem.setOldPrice(product.getOldPrice());
			cartItem.setQuantity(1);
			actualCartItems.add(cartItemRepository.save(cartItem));
		});

		// when
		Optional<CartItem> expectedCartItem = cartItemRepository.findByIdAndCartId(actualCartItems.get(0).getId(),
				carts.get(0).getId());

		// then
		assertAll(() -> assertThat(expectedCartItem).isNotEmpty(),
				() -> assertThat(expectedCartItem.get().getId()).isEqualTo(actualCartItems.get(0).getId()));
	}

	@Test
	@Order(5)
	@DisplayName("Find cartItem by cartId and productId test")
	public void givenCartItemList_whenFindByCartIdAndProductId_thenReturnCartItemOfCart() {
		// given
		List<CartItem> actualCartItems = new LinkedList<CartItem>();
		Store store = stores.get(0);
		IntStream.range(0, 2).mapToLong(Long::valueOf).forEach((e) -> {
			Product product = storeProducts.get(store).get((int) e);

			CartItem cartItem = new CartItem();
			cartItem.setCart(carts.get(0));
			cartItem.setProduct(product);
			cartItem.setName(product.getName());
			cartItem.setImage(product.getImage());
			cartItem.setPrice(product.getPrice());
			cartItem.setOldPrice(product.getOldPrice());
			cartItem.setQuantity(1);
			actualCartItems.add(cartItemRepository.save(cartItem));
		});

		// when
		Optional<CartItem> expectedCartItem = cartItemRepository.findByCartIdAndProductId(carts.get(0).getId(),
				storeProducts.get(store).get(0).getId());

		// then
		assertAll(() -> assertThat(expectedCartItem).isNotEmpty(),
				() -> assertThat(expectedCartItem.get().getId()).isEqualTo(actualCartItems.get(0).getId()));
	}

	@Test
	@Order(6)
	@DisplayName("Delete cartItem test")
	public void givenCartItem_whenDelete_thenRemoveCartItem() {
		// given
		Store store = stores.get(0);
		Product product = storeProducts.get(store).get(0);

		CartItem cartItem = new CartItem();
		cartItem.setCart(carts.get(0));
		cartItem.setProduct(product);
		cartItem.setName(product.getName());
		cartItem.setImage(product.getImage());
		cartItem.setPrice(product.getPrice());
		cartItem.setOldPrice(product.getOldPrice());
		cartItem.setQuantity(1);
		CartItem actualCartItem = cartItemRepository.save(cartItem);

		// when
		cartItemRepository.delete(actualCartItem);
		Optional<CartItem> expectedCartItem = cartItemRepository.findById(actualCartItem.getId());

		// then
		assertThat(expectedCartItem).isEmpty();
	}

}
