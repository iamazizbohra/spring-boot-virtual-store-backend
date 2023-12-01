package com.coedmaster.vstore.service.contract;

public interface IMobileVerificationService {
	void initVerification(String mobile);

	boolean verifyCode(String mobile, String verificationCode);
}
