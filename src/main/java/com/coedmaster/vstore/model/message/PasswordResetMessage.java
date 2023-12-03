package com.coedmaster.vstore.model.message;

import java.text.MessageFormat;

import com.coedmaster.vstore.model.contract.AbstractTextMessage;

public class PasswordResetMessage extends AbstractTextMessage {

	private String mobile;
	private String otp;
	private final String text = """
			Dear user, your OTP is {0}. Use this OTP to reset your password.
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
