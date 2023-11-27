package com.coedmaster.vstore.service;

import java.util.List;

import com.coedmaster.vstore.dto.AddressDto;
import com.coedmaster.vstore.model.Address;
import com.coedmaster.vstore.model.User;

public interface IAddressService {
	Address getAddress(Long addressId, User user);

	List<Address> getAddresses(User user);

	Address createAddress(User user, AddressDto payload);

	Address updateAddress(Long addressId, User user, AddressDto payload);

	Address setDefaultAddress(Long addressId, User user);

	void deleteAddress(Long addressId, User user);
}
