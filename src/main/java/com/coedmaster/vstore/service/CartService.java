package com.coedmaster.vstore.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coedmaster.vstore.model.Cart;
import com.coedmaster.vstore.model.Store;
import com.coedmaster.vstore.model.User;
import com.coedmaster.vstore.respository.CartRepository;

@Service
public class CartService implements ICartService {

	@Autowired
	private CartRepository cartRepository;

	@Override
	public Optional<Cart> getCart(User user) {
		return cartRepository.findByUserId(user.getId());
	}

	@Override
	public Optional<Cart> getCart(User user, Store store) {
		return cartRepository.findByUserIdAndStoreId(user.getId(), store.getId());
	}

	@Override
	public Cart createCart(User user, Store store) {
		Cart cart = new Cart();
		cart.setUser(user);
		cart.setStore(store);

		return cartRepository.save(cart);
	}

	@Override
	public void deleteCart(User user, Store store) {
		Optional<Cart> cartOptional = getCart(user, store);

		if (!cartOptional.isEmpty()) {
			cartRepository.deleteById(cartOptional.get().getId());
		}
	}

}
