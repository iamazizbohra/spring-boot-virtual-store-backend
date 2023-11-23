package com.coedmaster.vstore.controller.seller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coedmaster.vstore.model.IUserDetails;

@RestController("SellerHomeController")
@RequestMapping("/seller")
public class HomeController {

	@GetMapping("/home")
	public String index(@AuthenticationPrincipal IUserDetails userDetails) {
		return "Hello " + userDetails.getFirstName() + " from Seller Home";
	}
}
