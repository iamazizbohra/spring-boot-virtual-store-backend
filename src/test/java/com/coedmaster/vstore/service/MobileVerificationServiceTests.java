package com.coedmaster.vstore.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import org.jobrunr.configuration.JobRunr;
import org.jobrunr.storage.InMemoryStorageProvider;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MobileVerificationServiceTests {

	@Mock
	private RedisTemplate<String, String> redisTemplate;

	@Mock
	private ValueOperations<String, String> valueOperations;

	@InjectMocks
	private MobileVerificationService mobileVerificationService;

	@BeforeAll
	public static void beforeAll() {
		JobRunr.configure().useStorageProvider(new InMemoryStorageProvider()).useBackgroundJobServer().useDashboard()
				.initialize();
	}

	@Test
	@Order(1)
	@DisplayName("Init verification test")
	public void givenMobile_whenInitVerification_thenReturnVoid() {
		// given
		String mobile = "9999999999";

		given(redisTemplate.opsForValue()).willReturn(valueOperations);
		given(valueOperations.append(anyString(), anyString())).willReturn(0);
		given(redisTemplate.expire(anyString(), any())).willReturn(true);

		// when
		mobileVerificationService.initVerification(mobile);

		// then
		then(redisTemplate).should().opsForValue();
		then(valueOperations).should().append(anyString(), anyString());
		then(redisTemplate).should().expire(anyString(), any());
	}

	@Test
	@Order(2)
	@DisplayName("Generate code test")
	public void givenLength_whenGenerateCode_thenReturnCode() {
		// given
		int length = 4;

		// when
		String code = mobileVerificationService.generateCode(length);

		// then
		assertThat(code).isNotNull();
	}

	@Test
	@Order(3)
	@DisplayName("Generate cache key test")
	public void givenMobile_whenGenerateCacheKey_thenReturnKey() {
		// given
		String mobile = "9999999999";

		// when
		String key = mobileVerificationService.generateCacheKey(mobile);

		// then
		assertThat(key).isNotNull();
	}

	@Test
	@Order(4)
	@DisplayName("Store code to cache test")
	public void givenKeyAndCode_whenStoreCodeToCache_thenReturnVoid() {
		// given
		String key = "user:mvc:9999999999";
		String code = "4321";

		given(redisTemplate.opsForValue()).willReturn(valueOperations);
		given(valueOperations.append(anyString(), anyString())).willReturn(0);
		given(redisTemplate.expire(anyString(), any())).willReturn(true);

		// when
		mobileVerificationService.storeCodeToCache(key, code);

		// then
		then(redisTemplate).should().opsForValue();
		then(valueOperations).should().append(anyString(), anyString());
		then(redisTemplate).should().expire(anyString(), any());
	}

	@Test
	@Order(5)
	@DisplayName("Send code test")
	public void givenMobileAndCode_whenSendCode_thenReturnValueOfString() {
		// given
		String mobile = "9999999999";
		String code = "4321";

		// when
		String value = mobileVerificationService.sendCode(mobile, code);

		// then
		assertThat(value).isNotEmpty();
	}

	@Test
	@Order(6)
	@DisplayName("Verify code test")
	public void givenMobileAndCode_whenVerifyCode_thenReturnBoolean() {
		// given
		String mobile = "9999999999";
		String code = "4321";

		given(redisTemplate.opsForValue()).willReturn(valueOperations);
		given(valueOperations.get(anyString())).willReturn("4321");

		// when
		boolean value = mobileVerificationService.verifyCode(mobile, code);

		// then
		assertThat(value).isEqualTo(true);
	}

	@AfterAll
	public static void afterAll() {
		JobRunr.destroy();
	}

}
