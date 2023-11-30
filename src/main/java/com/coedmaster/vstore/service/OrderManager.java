package com.coedmaster.vstore.service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.coedmaster.vstore.domain.Address;
import com.coedmaster.vstore.domain.Cart;
import com.coedmaster.vstore.domain.CartItem;
import com.coedmaster.vstore.domain.CartTotalSummary;
import com.coedmaster.vstore.domain.Order;
import com.coedmaster.vstore.domain.OrderItem;
import com.coedmaster.vstore.domain.Product;
import com.coedmaster.vstore.domain.Store;
import com.coedmaster.vstore.domain.User;
import com.coedmaster.vstore.domain.specification.OrderSpecs;
import com.coedmaster.vstore.enums.OrderStatus;
import com.coedmaster.vstore.enums.UserType;
import com.coedmaster.vstore.exception.EntityNotFoundException;
import com.coedmaster.vstore.exception.UnallowedOperationException;
import com.coedmaster.vstore.respository.OrderItemRepository;
import com.coedmaster.vstore.respository.OrderRepository;
import com.coedmaster.vstore.service.contract.ICartService;
import com.coedmaster.vstore.service.contract.IOrderService;

import jakarta.transaction.Transactional;

@Service
public class OrderManager implements IOrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;

	@Autowired
	private ICartService cartManager;

	@Autowired
	private ProductService productService;

	@Override
	public Order getOrder(Long orderId, User user) {
		return orderRepository.findByIdAndUserId(orderId, user.getId())
				.orElseThrow(() -> new EntityNotFoundException("Order not found"));
	}

	@Override
	public Order getOrder(Long orderId, Store store) {
		return orderRepository.findByIdAndStoreId(orderId, store.getId())
				.orElseThrow(() -> new EntityNotFoundException("Order not found"));
	}

	@Override
	public Page<Order> getOrders(User user, Pageable pageable) {
		return orderRepository.findAllByUserId(user.getId(), pageable);
	}

	@Override
	public Page<Order> getOrders(Store store, Pageable pageable) {
		return orderRepository.findAllByStoreId(store.getId(), pageable);
	}

	@Override
	public Page<Order> getOrders(Store store, String status, Pageable pageable) {
		Specification<Order> specs = Specification.where(null);

		specs = specs.and(OrderSpecs.hasStoreId(store.getId()));

		if (ObjectUtils.isNotEmpty(status)) {
			specs = specs.and(OrderSpecs.hasStatus(OrderStatus.valueOf(status)));
		}

		return orderRepository.findAll(specs, pageable);
	}

	@Override
	public List<OrderItem> getOrderItems(Order order) {
		return orderItemRepository.findAllByOrderId(order.getId());
	}

	@Override
	@Transactional
	public Order createOrder(User user, Store store) {
		Cart cart = cartManager.getCart(user, store).orElseThrow(() -> new EntityNotFoundException("Cart not found"));

		List<CartItem> cartItems = cartManager.getCartItems(cart);

		Address shippingAddress = cart.getShippingAddress();

		if (ObjectUtils.isEmpty(shippingAddress)) {
			throw new EntityNotFoundException("Shipping address not found");
		}

		List<OrderItem> orderItems = new ArrayList<>();
		for (CartItem cartItem : cartItems) {
			Product product = productService.getProduct(cartItem.getProduct().getId(), store);

			if (cartItem.getQuantity() > product.getQuantity()) {
				throw new UnallowedOperationException(
						MessageFormat.format("You are allowed to buy only {0} unit(s) of {1} at a time",
								product.getQuantity(), product.getName()));
			}

			OrderItem orderItem = new OrderItem();
			orderItem.setCategory(product.getCategory());
			orderItem.setProduct(product);
			orderItem.setName(product.getName());
			orderItem.setImage(product.getImage());
			orderItem.setPrice(product.getPrice());
			orderItem.setQuantity(cartItem.getQuantity());
			orderItems.add(orderItem);
		}

		CartTotalSummary cartTotalSummary = cartManager.getCartTotalSummary(cart);

		Order order = new Order();
		order.setUser(user);
		order.setStore(store);
		order.setName(shippingAddress.getName());
		order.setMobile(shippingAddress.getMobile());
		order.setEmail(user.getEmail());
		order.setState(shippingAddress.getState());
		order.setCity(shippingAddress.getCity());
		order.setPincode(shippingAddress.getPincode());
		order.setLine1(shippingAddress.getLine1());
		order.setLine2(shippingAddress.getLine2());
		order.setLandmark(shippingAddress.getLandmark());
		order.setSubTotal(cartTotalSummary.getSubTotal());
		order.setShippingCharges(cartTotalSummary.getShippingCharges());
		order.setTotal(cartTotalSummary.getTotal());
		order.setStatus(OrderStatus.PENDING);
		order = orderRepository.save(order);

		for (OrderItem orderItem : orderItems) {
			orderItem.setOrder(order);
			orderItemRepository.save(orderItem);

			productService.updateProductQuantity(orderItem.getProduct().getId(), store,
					(orderItem.getProduct().getQuantity() - orderItem.getQuantity()));
		}

		cartManager.deleteCart(cart);

		return order;
	}

	@Override
	public Order updateOrderStatus(User user, OrderStatus orderStatus, Order order) {
		if (user.getUserType() == UserType.BUYER && orderStatus != OrderStatus.CANCELLED) {
			throw new UnallowedOperationException("Operation not allowed");
		}

		order.setStatus(orderStatus);

		return orderRepository.save(order);
	}

}
