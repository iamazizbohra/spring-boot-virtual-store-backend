package com.coedmaster.vstore.service;

import java.security.SecureRandom;

import org.springframework.stereotype.Service;

import com.coedmaster.vstore.service.contract.IOtpService;

@Service
public class OtpService implements IOtpService {

	@Override
	public String generateRandomPassword(int length) {
		SecureRandom rand = new SecureRandom();

		int bound = (int) Math.pow(10, length);

		return String.format("%04d", rand.nextInt(bound));
	}
}
