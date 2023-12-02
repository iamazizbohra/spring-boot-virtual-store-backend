package com.coedmaster.vstore.service.contract;

public interface IMobileVerificationService {
	void initVerification(String mobile);

	String generateCode(int length);

	/**
	 * This method generates a cache key, which is used to store the verification
	 * code in the cache
	 * 
	 * Ex: user:mvc:{0}, here mvc stands for Mobile Verification Code
	 *
	 * @author Aziz Bohra
	 */
	String generateCacheKey(String mobile);

	void storeCodeToCache(String key, String code);

	String sendCode(String mobile, String code);

	boolean verifyCode(String mobile, String code);
}
