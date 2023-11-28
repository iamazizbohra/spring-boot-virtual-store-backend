package com.coedmaster.vstore.service;

import java.util.Optional;

import com.coedmaster.vstore.model.Cart;
import com.coedmaster.vstore.model.CartItem;
import com.coedmaster.vstore.model.Product;

public interface ICartItemService {
	Optional<CartItem> getCartItem(Long cartItemId, Cart cart);

	Optional<CartItem> getCartItem(Cart cart, Product product);

	CartItem createCartItem(Cart cart, Product product, Integer quantity);

	CartItem updateCartItemQuantity(Cart cart, Product product, Integer quantity);
}
