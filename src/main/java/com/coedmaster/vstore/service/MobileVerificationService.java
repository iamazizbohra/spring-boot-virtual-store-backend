package com.coedmaster.vstore.service;

import java.security.SecureRandom;
import java.text.MessageFormat;
import java.time.Duration;

import org.apache.commons.lang3.StringUtils;
import org.jobrunr.jobs.JobId;
import org.jobrunr.jobs.context.JobContext;
import org.jobrunr.scheduling.BackgroundJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.coedmaster.vstore.service.contract.IMobileVerificationService;

@Service
public class MobileVerificationService implements IMobileVerificationService {

	private final int EXPIRE_IN_SECONDS = 180;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Override
	public void initVerification(String mobile) {
		String code = generateCode(4);

		String key = generateCacheKey(mobile);

		storeCodeToCache(key, code);

		sendCode(mobile, code);
	}

	@Override
	public String generateCode(int length) {
		SecureRandom rand = new SecureRandom();

		int bound = (int) Math.pow(10, length);

		return String.format("%04d", rand.nextInt(bound));
	}

	@Override
	public String generateCacheKey(String mobile) {
		String key = MessageFormat.format("user:mvc:{0}", mobile);

		return key;
	}

	@Override
	public void storeCodeToCache(String key, String code) {
		redisTemplate.opsForValue().append(key, code);
		redisTemplate.expire(key, Duration.ofSeconds(EXPIRE_IN_SECONDS));
	}

	@Override
	public String sendCode(String mobile, String code) {
		JobId jobId = BackgroundJob
				.<SmsService>enqueue(x -> x.sendMobileVerificationMessage(JobContext.Null, mobile, code));

		return jobId.toString();
	}

	@Override
	public boolean verifyCode(String mobile, String code) {
		String key = generateCacheKey(mobile);

		return StringUtils.equals(code, redisTemplate.opsForValue().get(key));
	}

}
