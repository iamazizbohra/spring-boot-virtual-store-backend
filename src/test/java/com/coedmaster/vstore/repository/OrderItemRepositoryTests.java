package com.coedmaster.vstore.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
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
import com.coedmaster.vstore.enums.OrderStatus;
import com.coedmaster.vstore.enums.UserType;
import com.coedmaster.vstore.model.Address;
import com.coedmaster.vstore.model.Category;
import com.coedmaster.vstore.model.OrderItem;
import com.coedmaster.vstore.model.Product;
import com.coedmaster.vstore.model.Role;
import com.coedmaster.vstore.model.Store;
import com.coedmaster.vstore.model.User;
import com.coedmaster.vstore.model.embeddable.FullName;
import com.coedmaster.vstore.respository.AddressRepository;
import com.coedmaster.vstore.respository.CategoryRepository;
import com.coedmaster.vstore.respository.OrderItemRepository;
import com.coedmaster.vstore.respository.OrderRepository;
import com.coedmaster.vstore.respository.ProductRepository;
import com.coedmaster.vstore.respository.RoleRepository;
import com.coedmaster.vstore.respository.StoreRepository;
import com.coedmaster.vstore.respository.UserRepository;
import com.github.javafaker.Faker;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderItemRepositoryTests {

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
	private OrderRepository orderRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;

	private final Faker faker = new Faker();

	private Role role;

	private User user;

	private Address address;

	private Store store;

	private List<Category> categories = new LinkedList<Category>();

	private List<Product> products = new LinkedList<Product>();

	private com.coedmaster.vstore.model.Order order;

	@BeforeEach
	public void beforeEach() {
		role = new Role();
		role.setName("ROLE_BUYER");
		role = roleRepository.save(role);

		FullName fullName = new FullName();
		fullName.setFirstName(faker.name().firstName());
		fullName.setLastName(faker.name().fullName());

		user = new User();
		user.setUuid(UUID.randomUUID());
		user.setUserType(UserType.BUYER);
		user.setFullName(fullName);
		user.setMobile(faker.phoneNumber().phoneNumber());
		user.setPassword(faker.internet().password());
		user.setEmail(faker.internet().emailAddress());
		user.setGender(Gender.MALE);
		user.setRoles(Collections.singletonList(role));
		user.setEnabled(true);
		user = userRepository.save(user);

		address = new Address();
		address.setUser(user);
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
		address = addressRepository.save(address);

		store = new Store();
		store.setUser(user);
		store.setName("Store 1");
		store.setCode("store1");
		store.setMobile(faker.phoneNumber().phoneNumber());
		store.setWhatsapp(faker.phoneNumber().phoneNumber());
		store.setEmail(faker.internet().emailAddress());
		store.setLatitude(faker.address().latitude());
		store.setLongitude(faker.address().longitude());
		store.setAddress(faker.address().fullAddress());
		store.setEnabled(true);
		store = storeRepository.save(store);

		IntStream.range(0, 2).mapToLong(Long::valueOf).forEach((e1) -> {
			Category category = new Category();
			category.setStore(store);
			category.setName(faker.name().firstName());
			category.setImage(faker.avatar().image());
			category.setEnabled(e1 == 0 ? false : true);
			categories.add(categoryRepository.save(category));

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
			products.add(productRepository.save(product));
		});

		Address shippingAddress = address;

		order = new com.coedmaster.vstore.model.Order();
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
		order = orderRepository.save(order);
	}

	@Test
	@Order(1)
	@DisplayName("Save orderItem test")
	public void givenOrderItem_whenSave_thenReturnSavedOrderItem() {
		// given
		Product product = products.get(0);

		OrderItem orderItem = new OrderItem();
		orderItem.setOrder(order);
		orderItem.setCategory(product.getCategory());
		orderItem.setProduct(product);
		orderItem.setName(product.getName());
		orderItem.setImage(product.getImage());
		orderItem.setPrice(product.getPrice());
		orderItem.setQuantity(1);

		// when
		OrderItem expectedOrderItem = orderItemRepository.save(orderItem);

		// then
		assertAll(() -> assertThat(expectedOrderItem).isNotNull(),
				() -> assertThat(expectedOrderItem.getId()).isGreaterThan(0));
	}

	@Test
	@Order(2)
	@DisplayName("Find all orderItems by orderId test")
	public void givenOrderItemList_whenFindAllByOrderId_thenReturnOrderItemsOfOrder() {
		// given
		IntStream.range(0, 2).mapToLong(Long::valueOf).forEach((e) -> {
			Product product = products.get((int) e);

			OrderItem orderItem = new OrderItem();
			orderItem.setOrder(order);
			orderItem.setCategory(product.getCategory());
			orderItem.setProduct(product);
			orderItem.setName(product.getName());
			orderItem.setImage(product.getImage());
			orderItem.setPrice(product.getPrice());
			orderItem.setQuantity(1);
			orderItemRepository.save(orderItem);
		});

		// when
		List<OrderItem> expectedOrderItems = orderItemRepository.findAllByOrderId(order.getId());

		// then
		assertThat(expectedOrderItems.size()).isEqualTo(2);
	}

}
