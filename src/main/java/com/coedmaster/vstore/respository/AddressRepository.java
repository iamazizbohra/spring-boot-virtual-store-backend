package com.coedmaster.vstore.respository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coedmaster.vstore.model.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
	Optional<Address> findByIdAndUserId(Long addressId, Long userId);

	List<Address> findAllByUserId(Long userId);
}
