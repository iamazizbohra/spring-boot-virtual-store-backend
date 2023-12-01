package com.coedmaster.vstore.domain;

import java.text.MessageFormat;

import com.coedmaster.vstore.domain.contract.AbstractTextMessage;

public class RegistrationOtpMessage extends AbstractTextMessage {

	private String mobile;
	private String otp;
	private final String text = """
			Dear user, your OTP for registration is {0}. Use this OTP to verify your mobile number.
			""";

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public String getText() {
		return MessageFormat.format(text, getOtp());
	}

}
