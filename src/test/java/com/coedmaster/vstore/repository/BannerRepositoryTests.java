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
import org.springframework.data.domain.Sort;

import com.coedmaster.vstore.enums.Gender;
import com.coedmaster.vstore.enums.UserType;
import com.coedmaster.vstore.model.Banner;
import com.coedmaster.vstore.model.Role;
import com.coedmaster.vstore.model.Store;
import com.coedmaster.vstore.model.User;
import com.coedmaster.vstore.model.embeddable.FullName;
import com.coedmaster.vstore.respository.BannerRepository;
import com.coedmaster.vstore.respository.RoleRepository;
import com.coedmaster.vstore.respository.StoreRepository;
import com.coedmaster.vstore.respository.UserRepository;
import com.github.javafaker.Faker;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BannerRepositoryTests {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private StoreRepository storeRepository;

	@Autowired
	private BannerRepository bannerRepository;

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
	@DisplayName("Save banner test")
	public void givenBanner_whenSave_thenReturnSavedBanner() {
		// given
		Banner banner = new Banner();
		banner.setStore(stores.get(0));
		banner.setTitle(faker.funnyName().name());
		banner.setImage(faker.avatar().image());
		banner.setSortOrder(Short.valueOf("0"));
		banner.setEnabled(true);

		// when
		Banner expectedBanner = bannerRepository.save(banner);

		// then
		assertAll(() -> assertThat(expectedBanner).isNotNull(),
				() -> assertThat(expectedBanner.getId()).isGreaterThan(0));
	}

	@Test
	@Order(2)
	@DisplayName("Update banner test")
	public void givenBanner_whenUpdate_thenReturnUpdatedBanner() {
		// given
		Banner banner = new Banner();
		banner.setStore(stores.get(0));
		banner.setTitle(faker.funnyName().name());
		banner.setImage(faker.avatar().image());
		banner.setSortOrder(Short.valueOf("0"));
		banner.setEnabled(true);
		Banner actualBanner = bannerRepository.save(banner);

		// when
		actualBanner.setTitle(faker.funnyName().name());
		actualBanner.setImage(faker.avatar().image());
		actualBanner.setSortOrder(Short.valueOf("1"));
		actualBanner.setEnabled(false);
		Banner expectedBanner = bannerRepository.save(actualBanner);

		// then
		assertAll(() -> assertThat(expectedBanner).isNotNull(),
				() -> assertThat(expectedBanner.getTitle()).isEqualTo(actualBanner.getTitle()),
				() -> assertThat(expectedBanner.getSortOrder()).isEqualTo(actualBanner.getSortOrder()),
				() -> assertThat(expectedBanner.isEnabled()).isEqualTo(actualBanner.isEnabled()));
	}

	@Test
	@Order(3)
	@DisplayName("Find all banners by storeId test")
	public void givenBannerList_whenFindAllByStoreId_thenReturnBannersOfStore() {
		// given
		Banner banner1 = new Banner();
		banner1.setStore(stores.get(0));
		banner1.setTitle(faker.funnyName().name());
		banner1.setImage(faker.avatar().image());
		banner1.setSortOrder(Short.valueOf(String.valueOf(0)));
		banner1.setEnabled(true);
		bannerRepository.save(banner1);

		IntStream.range(0, 3).mapToLong(Long::valueOf).forEach((e) -> {
			Banner banner = new Banner();
			banner.setStore(stores.get(1));
			banner.setTitle(faker.funnyName().name());
			banner.setImage(faker.avatar().image());
			banner.setSortOrder(Short.valueOf(String.valueOf(e)));
			banner.setEnabled(e == 0 ? false : true);
			bannerRepository.save(banner);
		});

		// when
		List<Banner> expectedBanners = bannerRepository.findAllByStoreId(stores.get(1).getId(),
				Sort.by(Sort.Direction.DESC, "sortOrder"));

		// then
		assertThat(expectedBanners.size()).isEqualTo(3);
	}

	@Test
	@Order(4)
	@DisplayName("Find banner by Id and storeId test")
	public void givenBannerList_whenFindByIdAndStoreId_thenReturnBannerOfStore() {
		// given
		Banner banner1 = new Banner();
		banner1.setStore(stores.get(0));
		banner1.setTitle(faker.funnyName().name());
		banner1.setImage(faker.avatar().image());
		banner1.setSortOrder(Short.valueOf(String.valueOf(0)));
		banner1.setEnabled(true);
		Banner actualBanner = bannerRepository.save(banner1);

		IntStream.range(0, 3).mapToLong(Long::valueOf).forEach((e) -> {
			Banner banner = new Banner();
			banner.setStore(stores.get(1));
			banner.setTitle(faker.funnyName().name());
			banner.setImage(faker.avatar().image());
			banner.setSortOrder(Short.valueOf(String.valueOf(e)));
			banner.setEnabled(e == 0 ? false : true);
			bannerRepository.save(banner);
		});

		// when
		Optional<Banner> expectedBanner = bannerRepository.findByIdAndStoreId(actualBanner.getId(),
				stores.get(0).getId());

		// then
		assertAll(() -> assertThat(expectedBanner).isNotEmpty(),
				() -> assertThat(expectedBanner.get().getId()).isEqualTo(actualBanner.getId()));
	}

	@Test
	@Order(5)
	@DisplayName("Find all banners by storeId and enabled test")
	public void givenBannerList_whenFindAllByStoreIdAndEnabled_thenReturnEnabledBannersOfStore() {
		// given
		Banner banner1 = new Banner();
		banner1.setStore(stores.get(0));
		banner1.setTitle(faker.funnyName().name());
		banner1.setImage(faker.avatar().image());
		banner1.setSortOrder(Short.valueOf(String.valueOf(0)));
		banner1.setEnabled(true);
		bannerRepository.save(banner1);

		IntStream.range(0, 3).mapToLong(Long::valueOf).forEach((e) -> {
			Banner banner = new Banner();
			banner.setStore(stores.get(1));
			banner.setTitle(faker.funnyName().name());
			banner.setImage(faker.avatar().image());
			banner.setSortOrder(Short.valueOf(String.valueOf(e)));
			banner.setEnabled(e == 0 ? false : true);
			bannerRepository.save(banner);
		});

		// when
		List<Banner> expectedBanners = bannerRepository.findAllByStoreIdAndEnabled(stores.get(1).getId(), true,
				Sort.by(Sort.Direction.DESC, "sortOrder"));

		// then
		assertThat(expectedBanners.size()).isEqualTo(2);
	}

	@Test
	@Order(6)
	@DisplayName("Delete banner test")
	public void givenBanner_whenDelete_thenRemoveBanner() {
		// given
		Banner banner = new Banner();
		banner.setStore(stores.get(0));
		banner.setTitle(faker.funnyName().name());
		banner.setImage(faker.avatar().image());
		banner.setSortOrder(Short.valueOf(String.valueOf(0)));
		banner.setEnabled(true);
		Banner actualBanner = bannerRepository.save(banner);

		// When
		bannerRepository.delete(banner);
		Optional<Banner> expectedBanner = bannerRepository.findById(actualBanner.getId());

		// then
		assertThat(expectedBanner).isEmpty();
	}

}
