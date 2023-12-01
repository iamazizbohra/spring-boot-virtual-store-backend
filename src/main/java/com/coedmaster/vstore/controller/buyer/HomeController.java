package com.coedmaster.vstore.controller.buyer;

import org.jobrunr.jobs.context.JobContext;
import org.jobrunr.scheduling.BackgroundJob;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coedmaster.vstore.domain.contract.IUserDetails;
import com.coedmaster.vstore.service.SmsService;

@RestController("BuyerHomeController")
@RequestMapping("/buyer")
public class HomeController {

	@GetMapping("/home")
	public String index(@AuthenticationPrincipal IUserDetails userDetails) {
		BackgroundJob.<SmsService>enqueue(x -> x.sendRegistrationOtpMessage(JobContext.Null, "9432132353", "1234"));

		return "Hello " + userDetails.getFirstName() + " from Buyer Home";
	}
}
