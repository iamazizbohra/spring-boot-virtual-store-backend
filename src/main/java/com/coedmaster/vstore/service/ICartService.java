package com.coedmaster.vstore.service;

import java.util.Optional;

import com.coedmaster.vstore.model.Cart;
import com.coedmaster.vstore.model.Store;
import com.coedmaster.vstore.model.User;

public interface ICartService {
	Optional<Cart> getCart(User user);

	Optional<Cart> getCart(User user, Store store);

	Cart createCart(User user, Store store);

	void deleteCart(User user, Store store);
}
