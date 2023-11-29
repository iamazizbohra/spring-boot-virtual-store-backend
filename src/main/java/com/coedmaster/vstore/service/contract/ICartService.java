package com.coedmaster.vstore.service.contract;

import java.util.List;
import java.util.Optional;

import com.coedmaster.vstore.domain.Address;
import com.coedmaster.vstore.domain.Cart;
import com.coedmaster.vstore.domain.CartItem;
import com.coedmaster.vstore.domain.CartTotalSummary;
import com.coedmaster.vstore.domain.Product;
import com.coedmaster.vstore.domain.Store;
import com.coedmaster.vstore.domain.User;

public interface ICartService {
	Optional<Cart> getCart(User user, Store store);

	List<CartItem> getCartItems(Cart cart);

	CartItem addCartItem(User user, Store store, Product product, Integer quantity);

	void removeCartItem(Long cartItemId, User user, Store store);

	void updateCartShippingAddress(User user, Store store, Address address);

	CartTotalSummary getCartTotalSummary(Cart cart);

	void deleteCart(Cart cart);
}
