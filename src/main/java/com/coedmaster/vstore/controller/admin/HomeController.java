package com.coedmaster.vstore.controller.admin;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coedmaster.vstore.model.UserDetailsImpl;

@RestController("AdminHomeController")
@RequestMapping("/api/admin")
public class HomeController {

	@GetMapping("/home")
	public String index(@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return "Hello " + userDetails.getFirstName() + " from Admin Home";
	}
}
