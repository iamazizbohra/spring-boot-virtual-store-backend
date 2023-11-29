package com.coedmaster.vstore.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.coedmaster.vstore.enums.OrderStatus;
import com.coedmaster.vstore.model.Order;
import com.coedmaster.vstore.model.OrderItem;
import com.coedmaster.vstore.model.Store;
import com.coedmaster.vstore.model.User;

public interface IOrderService {
	Order getOrder(Long orderId, User user);

	Order getOrder(Long orderId, Store store);

	Page<Order> getOrders(User user, Pageable pageable);

	Page<Order> getOrders(Store store, Pageable pageable);

	List<OrderItem> getOrderItems(Order order);

	Order createOrder(User user, Store store);

	Order updateOrderStatus(User user, OrderStatus orderStatus, Order order);
}
