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

import com.coedmaster.vstore.model.Banner;
import com.coedmaster.vstore.model.Store;
import com.coedmaster.vstore.service.BannerService;
import com.coedmaster.vstore.service.StoreService;

@WebMvcTest(controllers = BannerController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class BannerControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private StoreService storeService;

	@MockBean
	private BannerService bannerService;
	
	@MockBean
	private ModelMapper modelMapper;

	@Test
	@DisplayName("Get banners test")
	public void givenStoreId_whenGetBanners_thenResponseOk() throws Exception {
		// given
		List<Banner> banners = new ArrayList<Banner>();
		Banner b1 = new Banner();
		b1.setId(1L);
		b1.setTitle("Banner 1");
		b1.setSortOrder(Short.valueOf("0"));
		b1.setEnabled(true);
		banners.add(b1);

		Banner b2 = new Banner();
		b2.setId(2L);
		b2.setTitle("Banner 2");
		b2.setSortOrder(Short.valueOf("1"));
		b2.setEnabled(true);
		banners.add(b2);

		given(storeService.getStore(1L)).willReturn(new Store());
		given(bannerService.getBanners(any(Store.class), anyBoolean())).willReturn(banners);

		// when
		RequestBuilder request = get("/buyer/store/{storeId}/banner", 1L);
		ResultActions response = mockMvc.perform(request);

		// then
		response.andDo(log()).andExpect(status().isOk()).andExpect(jsonPath("$.data.length()").value(2));
	}

}
