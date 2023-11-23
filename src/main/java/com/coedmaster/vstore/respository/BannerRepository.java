package com.coedmaster.vstore.respository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.coedmaster.vstore.model.Banner;

public interface BannerRepository extends JpaRepository<Banner, Long> {
	Optional<Banner> findByIdAndStoreId(Long id, Long storeId);
	
	List<Banner> findAllByStoreId(Long id, Sort sort);
}
