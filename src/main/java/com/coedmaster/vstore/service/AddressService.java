package com.coedmaster.vstore.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coedmaster.vstore.dto.AddressDto;
import com.coedmaster.vstore.exception.EntityNotFoundException;
import com.coedmaster.vstore.exception.UnallowedOperationException;
import com.coedmaster.vstore.model.Address;
import com.coedmaster.vstore.model.User;
import com.coedmaster.vstore.respository.AddressRepository;
import com.coedmaster.vstore.service.contract.IAddressService;

@Service
public class AddressService implements IAddressService {

	@Autowired
	private AddressRepository addressRepository;

	@Override
	public Address getAddress(Long addressId, User user) {
		return addressRepository.findByIdAndUserId(addressId, user.getId())
				.orElseThrow(() -> new EntityNotFoundException("Address not found"));
	}

	@Override
	public List<Address> getAddresses(User user) {
		return addressRepository.findAllByUserId(user.getId());
	}

	@Override
	public Address createAddress(User user, AddressDto payload) {
		Address address = new Address();
		address.setUser(user);
		address.setTitle(payload.getTitle());
		address.setName(payload.getName());
		address.setMobile(payload.getMobile());
		address.setState(payload.getState());
		address.setCity(payload.getCity());
		address.setPincode(payload.getPincode());
		address.setLine1(payload.getLine1());
		address.setLine2(payload.getLine2());
		address.setLandmark(payload.getLandmark());

		if (getAddresses(user).size() == 0) {
			address.setDefault(true);
		} else {
			address.setDefault(false);
		}

		return addressRepository.save(address);
	}

	@Override
	public Address updateAddress(Long addressId, User user, AddressDto payload) {
		Address address = getAddress(addressId, user);
		address.setTitle(payload.getTitle());
		address.setName(payload.getName());
		address.setMobile(payload.getMobile());
		address.setState(payload.getState());
		address.setCity(payload.getCity());
		address.setPincode(payload.getPincode());
		address.setLine1(payload.getLine1());
		address.setLine2(payload.getLine2());
		address.setLandmark(payload.getLandmark());

		return addressRepository.save(address);
	}

	@Override
	public Address updateDefaultAddress(Long addressId, User user) {
		List<Address> addresses = getAddresses(user);
		addresses.stream().forEach((address) -> {
			address.setDefault(false);

			addressRepository.save(address);
		});

		Address address = getAddress(addressId, user);
		address.setDefault(true);

		return addressRepository.save(address);
	}

	@Override
	public void deleteAddress(Long addressId, User user) {
		Address address = getAddress(addressId, user);

		if (address.isDefault()) {
			throw new UnallowedOperationException("Can not delete default address");
		}

		addressRepository.delete(address);
	}

}
