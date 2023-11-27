package com.coedmaster.vstore.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coedmaster.vstore.exception.EntityNotFoundException;
import com.coedmaster.vstore.model.Cart;
import com.coedmaster.vstore.model.CartItem;
import com.coedmaster.vstore.model.Product;
import com.coedmaster.vstore.respository.CartItemRepository;

@Service
public class CartItemService implements ICartItemService {

	@Autowired
	private CartItemRepository cartItemRepository;

	@Override
	public Optional<CartItem> getCartItem(Long cartItemId, Cart cart) {
		return cartItemRepository.findByIdAndCartId(cartItemId, cart.getId());
	}

	@Override
	public Optional<CartItem> getCartItem(Cart cart, Product product) {
		return cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId());
	}

	@Override
	public CartItem createCartItem(Cart cart, Product product, Integer quantity) {
		CartItem cartItem = new CartItem();
		cartItem.setCart(cart);
		cartItem.setProduct(product);
		cartItem.setName(product.getName());
		cartItem.setImage(product.getImage());
		cartItem.setPrice(product.getPrice());
		cartItem.setOldPrice(product.getOldPrice());
		cartItem.setQuantity(quantity);

		return cartItemRepository.save(cartItem);
	}

	@Override
	public CartItem updateCartItemQuantity(Cart cart, Product product, Integer quantity) {
		Optional<CartItem> cartItemOptional = getCartItem(cart, product);

		if (cartItemOptional.isEmpty()) {
			throw new EntityNotFoundException("Cart item not found");
		}

		CartItem cartItem = cartItemOptional.get();
		cartItem.setQuantity(quantity);

		return cartItemRepository.save(cartItem);

	}

	@Override
	public void deleteCartItem(Cart cart, Product product) {
		Optional<CartItem> cartItemOptional = getCartItem(cart, product);

		if (!cartItemOptional.isEmpty()) {
			cartItemRepository.deleteById(cartItemOptional.get().getId());
		}
	}

}
