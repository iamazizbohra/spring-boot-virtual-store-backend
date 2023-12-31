package com.coedmaster.vstore.service.contract;

import java.util.List;
import java.util.Optional;

import com.coedmaster.vstore.model.Address;
import com.coedmaster.vstore.model.Cart;
import com.coedmaster.vstore.model.CartItem;
import com.coedmaster.vstore.model.CartTotalSummary;
import com.coedmaster.vstore.model.Product;
import com.coedmaster.vstore.model.Store;
import com.coedmaster.vstore.model.User;

public interface ICartService {
	Optional<Cart> getCart(User user, Store store);

	List<CartItem> getCartItems(Cart cart);

	CartItem addCartItem(User user, Store store, Product product, Integer quantity);

	void removeCartItem(Long cartItemId, User user, Store store);

	void updateCartShippingAddress(User user, Store store, Address address);

	CartTotalSummary getCartTotalSummary(Cart cart);

	void deleteCart(Cart cart);
}
