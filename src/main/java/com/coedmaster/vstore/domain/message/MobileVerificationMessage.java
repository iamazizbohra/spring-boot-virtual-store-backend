package com.coedmaster.vstore.domain.message;

import java.text.MessageFormat;

import com.coedmaster.vstore.domain.contract.AbstractTextMessage;

public class MobileVerificationMessage extends AbstractTextMessage {

	private String mobile;
	private String otp;
	private final String text = """
			Dear user, your mobile verification code is {0}. Use this code to verify your mobile number.
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
