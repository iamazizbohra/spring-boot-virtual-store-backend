package com.coedmaster.vstore.service;

import com.coedmaster.vstore.dto.request.RegistrationRequestDto;
import com.coedmaster.vstore.model.User;

public interface RegistrationService {
	User registerAdmin(RegistrationRequestDto payload);
	
	User registerBuyer(RegistrationRequestDto payload);
	
	User registerSeller(RegistrationRequestDto payload);
}
