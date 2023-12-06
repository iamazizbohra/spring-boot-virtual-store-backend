package com.coedmaster.vstore.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coedmaster.vstore.exception.EntityNotFoundException;
import com.coedmaster.vstore.model.Address;
import com.coedmaster.vstore.model.Cart;
import com.coedmaster.vstore.model.CartItem;
import com.coedmaster.vstore.model.CartTotalSummary;
import com.coedmaster.vstore.model.Product;
import com.coedmaster.vstore.model.Store;
import com.coedmaster.vstore.model.User;
import com.coedmaster.vstore.respository.CartItemRepository;
import com.coedmaster.vstore.respository.CartRepository;
import com.coedmaster.vstore.service.contract.ICartService;

@Service
public class CartManager implements ICartService {

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private CartItemRepository cartItemRepository;
	
	@Autowired
	private AddressService addressService;

	@Override
	public Optional<Cart> getCart(User user, Store store) {
		return cartRepository.findByUserIdAndStoreId(user.getId(), store.getId());
	}

	@Override
	public List<CartItem> getCartItems(Cart cart) {
		return cartItemRepository.findAllByCartId(cart.getId());
	}

	@Override
	public CartItem addCartItem(User user, Store store, Product product, Integer quantity) {
		Cart cart;

		CartItem cartItem;

		Optional<Cart> cartOptional = getCart(user);
		if (cartOptional.isEmpty()) {
			cart = createCart(user, store);

			Optional<Address> defaultAddressOptional = addressService.getAddresses(user).stream().filter(e -> e.isDefault())
					.findAny();

			if (!defaultAddressOptional.isEmpty()) {
				updateCartShippingAddress(user, store, defaultAddressOptional.get());
			}
		} else {
			cart = cartOptional.get();

			if (!cart.getStore().getId().equals(store.getId())) {
				deleteCart(cart);

				cart = createCart(user, store);
			}
		}

		Optional<CartItem> cartItemOptional = getCartItem(cart, product);
		if (cartItemOptional.isEmpty()) {
			cartItem = createCartItem(cart, product, quantity);
		} else {
			cartItem = cartItemOptional.get();

			cartItem = updateCartItemQuantity(cartItem, quantity);
		}

		return cartItem;
	}

	@Override
	public void removeCartItem(Long cartItemId, User user, Store store) {
		Cart cart;

		CartItem cartItem;

		Optional<Cart> cartOptional = getCart(user, store);
		if (cartOptional.isEmpty()) {
			throw new EntityNotFoundException("Cart not found");
		} else {
			cart = cartOptional.get();
		}

		Optional<CartItem> cartItemOptional = getCartItem(cartItemId, cart);
		if (cartItemOptional.isEmpty()) {
			throw new EntityNotFoundException("Cart item not found");
		} else {
			cartItem = cartItemOptional.get();

			deleteCartItem(cartItem);
		}

		List<CartItem> cartItems = getCartItems(cart);
		if (cartItems.size() == 0) {
			deleteCart(cart);
		}
	}

	@Override
	public void updateCartShippingAddress(User user, Store store, Address address) {
		Cart cart = getCart(user, store).orElseThrow(() -> new EntityNotFoundException("Cart not found"));
		cart.setShippingAddress(address);

		cartRepository.save(cart);
	}

	@Override
	public CartTotalSummary getCartTotalSummary(Cart cart) {
		List<CartItem> cartItems = getCartItems(cart);

		Integer subTotal = 0;
		Integer shippingCharges = 0;

		for (CartItem cartItem : cartItems) {
			Integer price = cartItem.getPrice();
			Integer quantity = cartItem.getQuantity();

			subTotal += price * quantity;
		}

		return CartTotalSummary.builder().subTotal(subTotal).shippingCharges(shippingCharges)
				.total(subTotal + shippingCharges).build();
	}

	@Override
	public void deleteCart(Cart cart) {
		cartRepository.delete(cart);
	}

	private Optional<Cart> getCart(User user) {
		return cartRepository.findByUserId(user.getId());
	}

	private Cart createCart(User user, Store store) {
		Cart cart = new Cart();
		cart.setUser(user);
		cart.setStore(store);

		return cartRepository.save(cart);
	}

	private Optional<CartItem> getCartItem(Long cartItemId, Cart cart) {
		return cartItemRepository.findByIdAndCartId(cartItemId, cart.getId());
	}

	private Optional<CartItem> getCartItem(Cart cart, Product product) {
		return cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId());
	}

	private CartItem createCartItem(Cart cart, Product product, Integer quantity) {
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

	private CartItem updateCartItemQuantity(CartItem cartItem, Integer quantity) {
		cartItem.setQuantity(quantity);

		return cartItemRepository.save(cartItem);

	}

	private void deleteCartItem(CartItem cartItem) {
		cartItemRepository.delete(cartItem);
	}
}
