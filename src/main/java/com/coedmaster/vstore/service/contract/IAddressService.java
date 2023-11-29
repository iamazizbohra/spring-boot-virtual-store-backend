package com.coedmaster.vstore.service.contract;

import java.util.List;

import com.coedmaster.vstore.domain.Address;
import com.coedmaster.vstore.domain.User;
import com.coedmaster.vstore.dto.AddressDto;

public interface IAddressService {
	Address getAddress(Long addressId, User user);

	List<Address> getAddresses(User user);

	Address createAddress(User user, AddressDto payload);

	Address updateAddress(Long addressId, User user, AddressDto payload);

	Address updateDefaultAddress(Long addressId, User user);

	void deleteAddress(Long addressId, User user);
}
