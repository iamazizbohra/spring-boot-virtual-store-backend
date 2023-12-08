package com.coedmaster.vstore.controller.buyer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;

import com.coedmaster.vstore.model.Category;
import com.coedmaster.vstore.model.Store;
import com.coedmaster.vstore.service.CategoryService;
import com.coedmaster.vstore.service.StoreService;

@WebMvcTest(controllers = CategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class CategoryControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private StoreService storeService;

	@MockBean
	private CategoryService categoryService;

	@MockBean
	private ModelMapper modelMapper;

	@Test
	@DisplayName("Get categories test")
	public void givenStoreId_whenGetCategories_thenResponseOk() throws Exception {
		// given
		List<Category> categories = new ArrayList<Category>();
		Category c1 = new Category();
		c1.setId(1L);
		c1.setName("Category 1");
		c1.setEnabled(true);
		categories.add(c1);

		Category c2 = new Category();
		c2.setId(2L);
		c2.setName("Category 2");
		c2.setEnabled(true);
		categories.add(c2);

		given(storeService.getStore(1L)).willReturn(new Store());
		given(categoryService.getCategories(any(Store.class), anyBoolean())).willReturn(categories);

		// when
		RequestBuilder request = get("/buyer/store/{storeId}/category", 1L);
		ResultActions response = mockMvc.perform(request);

		// then
		response.andDo(log()).andExpect(status().isOk()).andExpect(jsonPath("$.data.length()").value(2));
	}

}
