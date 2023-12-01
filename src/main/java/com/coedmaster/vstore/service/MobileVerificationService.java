package com.coedmaster.vstore.service;

import java.text.MessageFormat;
import java.time.Duration;

import org.apache.commons.lang3.StringUtils;
import org.jobrunr.jobs.context.JobContext;
import org.jobrunr.scheduling.BackgroundJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.coedmaster.vstore.service.contract.IMobileVerificationService;
import com.coedmaster.vstore.service.contract.IOtpService;

@Service
public class MobileVerificationService implements IMobileVerificationService {

	private final int EXPIRE_IN_SECONDS = 180;

	@Autowired
	private IOtpService otpService;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Override
	public void initVerification(String mobile) {
		String verificationCode = otpService.generateRandomPassword(4);

		String key = MessageFormat.format("user:mvc:{0}", mobile);
		redisTemplate.opsForValue().append(key, verificationCode);
		redisTemplate.expire(key, Duration.ofSeconds(EXPIRE_IN_SECONDS));

		BackgroundJob.<SmsService>enqueue(
				x -> x.sendMobileVerificationMessage(JobContext.Null, mobile, verificationCode));
	}

	@Override
	public boolean verifyCode(String mobile, String verificationCode) {
		String key = MessageFormat.format("user:mvc:{0}", mobile);

		return StringUtils.equals(verificationCode, redisTemplate.opsForValue().get(key));
	}

}
