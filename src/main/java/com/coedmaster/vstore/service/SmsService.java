package com.coedmaster.vstore.service;

import org.jobrunr.jobs.annotations.Job;
import org.jobrunr.jobs.context.JobContext;
import org.jobrunr.jobs.context.JobDashboardProgressBar;
import org.springframework.stereotype.Service;

import com.coedmaster.vstore.domain.contract.AbstractTextMessage;
import com.coedmaster.vstore.domain.message.MobileNumberVerificationMessage;
import com.coedmaster.vstore.service.contract.ISmsService;

@Service
public class SmsService implements ISmsService {

	@Override
	@Job(name = "Registration OTP Message Job")
	public void sendRegistrationOtpMessage(JobContext jobContext, String mobile, String otp) {
		JobDashboardProgressBar progressBar = jobContext.progressBar(6);

		jobContext.logger().info("Creating message");
		var message = new MobileNumberVerificationMessage();
		progressBar.increaseByOne();

		jobContext.logger().info("Setting mobile number to: " + mobile);
		message.setMobile(mobile);
		progressBar.increaseByOne();

		jobContext.logger().info("Setting OTP to: " + otp);
		message.setOtp(otp);
		progressBar.increaseByOne();

		jobContext.logger().info("Message created successfully");
		progressBar.increaseByOne();

		jobContext.logger().info("Sending message: ");
		progressBar.increaseByOne();
		sendMessage(message);
		jobContext.logger().info(message.getText());
		jobContext.logger().info("Message sent successfully");
		progressBar.increaseByOne();
	}

	private void sendMessage(AbstractTextMessage message) {
		// actual code to send message

	}

}
