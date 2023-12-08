package com.coedmaster.vstore.controller.seller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = HomeController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class HomeControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	@DisplayName("Index test")
	public void givenHomeURI_whenIndex_thenResponseOk() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.get("/seller/home");

		ResultActions response = mockMvc.perform(request);

		response.andDo(MockMvcResultHandlers.log()).andExpect(MockMvcResultMatchers.status().isOk());
	}

}
