package com.coedmaster.vstore.controller.buyer;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;

import com.coedmaster.vstore.config.DtoConfig;
import com.coedmaster.vstore.model.Category;
import com.coedmaster.vstore.model.Product;
import com.coedmaster.vstore.model.Store;
import com.coedmaster.vstore.service.CategoryService;
import com.coedmaster.vstore.service.ProductService;
import com.coedmaster.vstore.service.StoreService;

@WebMvcTest(controllers = ProductController.class)
@Import(DtoConfig.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CategoryService categoryService;

	@MockBean
	private StoreService storeService;

	@MockBean
	private ProductService productService;

	@Mock
	public Page<Product> productsPage;

	private Store store;

	private List<Category> categories = new ArrayList<Category>();

	private Map<Store, List<Product>> storeProducts = new HashMap<Store, List<Product>>();

	private Map<Store, List<Product>> storeProductsOfCategory1 = new HashMap<Store, List<Product>>();

	@BeforeEach
	public void beforeEach() {
		store = new Store();
		store.setId(1L);
		store.setName("Store 1");
		storeProducts.put(store, new LinkedList<Product>());
		storeProductsOfCategory1.put(store, new LinkedList<Product>());

		Category category1 = new Category();
		category1.setId(1L);
		category1.setStore(store);
		category1.setName("Category 1");
		categories.add(category1);

		Product product1 = new Product();
		product1.setId(1L);
		product1.setStore(store);
		product1.setCategory(category1);
		product1.setName("Product 1 of Category 1");
		storeProducts.get(store).add(product1);
		storeProductsOfCategory1.get(store).add(product1);

		Product product2 = new Product();
		product2.setId(2L);
		product2.setStore(store);
		product2.setCategory(category1);
		product2.setName("Product 2 of Category 1");
		storeProducts.get(store).add(product2);
		storeProductsOfCategory1.get(store).add(product2);

		Category category2 = new Category();
		category2.setId(2L);
		category2.setStore(store);
		category2.setName("Category 2");
		categories.add(category2);

		Product product3 = new Product();
		product3.setId(3L);
		product3.setStore(store);
		product3.setCategory(category2);
		product3.setName("Product 1 of Category 2");
		storeProducts.get(store).add(product3);

		Product product4 = new Product();
		product4.setId(4L);
		product4.setStore(store);
		product4.setCategory(category2);
		product4.setName("Product 2 of Category 2");
		storeProducts.get(store).add(product4);
	}

	@Test
	@Order(1)
	@DisplayName("Get product of store test")
	public void givenStoreIdAndProductId_whenGetProduct_thenResponseOk() throws Exception {
		// given
		given(storeService.getStore(1L)).willReturn(store);
		given(productService.getProduct(1L, store, true)).willReturn(storeProducts.get(store).get(0));

		// when
		RequestBuilder request = get("/buyer/store/{storeId}/product/{productId}", 1L, 1L);
		ResultActions response = mockMvc.perform(request);

		// then
		response.andDo(log()).andExpect(status().isOk()).andExpect(jsonPath("$.data", is(notNullValue())));
	}

	@Test
	@Order(2)
	@DisplayName("Get products of store test")
	public void givenStoreId_whenGetProducts_thenResponseOk() throws Exception {
		// given
		given(storeService.getStore(1L)).willReturn(store);
		given(productService.getProducts(any(Store.class), anyBoolean(), any(Pageable.class))).willReturn(productsPage);
		given(productsPage.getContent()).willReturn(storeProducts.get(store));
		given(productsPage.getNumber()).willReturn(0);
		given(productsPage.getTotalElements()).willReturn(4L);
		given(productsPage.getTotalPages()).willReturn(1);

		// when
		RequestBuilder request = get("/buyer/store/{storeId}/product", 1L);
		ResultActions response = mockMvc.perform(request);

		// then
		response.andDo(log()).andExpect(status().isOk()).andExpect(jsonPath("$.data", is(notNullValue())))
				.andExpect(jsonPath("$.data.products.length()").value(4));
	}

	@Test
	@Order(3)
	@DisplayName("Get products of store by categories test")
	public void givenStoreIdAndCategoryIds_whenGetProducts_thenResponseOk() throws Exception {
		// given
		given(storeService.getStore(1L)).willReturn(store);
		given(categoryService.getCategories(anyList(), any(Store.class), anyBoolean()))
				.willReturn(List.of(categories.get(0)));
		given(productService.getProducts(any(Store.class), anyList(), anyBoolean(), any(Pageable.class)))
				.willReturn(productsPage);
		given(productsPage.getContent()).willReturn(storeProductsOfCategory1.get(store));
		given(productsPage.getNumber()).willReturn(0);
		given(productsPage.getTotalElements()).willReturn(2L);
		given(productsPage.getTotalPages()).willReturn(1);

		// when
		RequestBuilder request = get("/buyer/store/{storeId}/product", 1L).param("categoryIds", new String[] { "1" });
		ResultActions response = mockMvc.perform(request);

		// then
		response.andDo(log()).andExpect(status().isOk()).andExpect(jsonPath("$.data", is(notNullValue())))
				.andExpect(jsonPath("$.data.products.length()").value(2));
	}

}
