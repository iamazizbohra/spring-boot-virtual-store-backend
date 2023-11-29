package com.coedmaster.vstore.controller.buyer;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coedmaster.vstore.domain.IUserDetails;

@RestController("BuyerHomeController")
@RequestMapping("/buyer")
public class HomeController {
	
	@GetMapping("/home")
	public String index(@AuthenticationPrincipal IUserDetails userDetails) {
		return "Hello " + userDetails.getFirstName() + " from Buyer Home";
	}
}
