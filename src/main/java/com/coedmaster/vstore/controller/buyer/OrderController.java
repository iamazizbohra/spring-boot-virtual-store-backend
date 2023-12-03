package com.coedmaster.vstore.controller.buyer;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.coedmaster.vstore.dto.CreateOrderDto;
import com.coedmaster.vstore.dto.OrderDetailsDto;
import com.coedmaster.vstore.dto.OrderDto;
import com.coedmaster.vstore.dto.OrderItemDto;
import com.coedmaster.vstore.dto.response.SuccessResponseDto;
import com.coedmaster.vstore.model.Order;
import com.coedmaster.vstore.model.OrderItem;
import com.coedmaster.vstore.model.Store;
import com.coedmaster.vstore.model.User;
import com.coedmaster.vstore.service.contract.IAuthenticationService;
import com.coedmaster.vstore.service.contract.IOrderService;
import com.coedmaster.vstore.service.contract.IStoreService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

@RestController("BuyerOrderController")
@RequestMapping("/buyer")
public class OrderController {

	@Autowired
	private IAuthenticationService authenticationService;

	@Autowired
	private IStoreService storeService;

	@Autowired
	private IOrderService orderManager;

	@Autowired
	private Validator validator;

	@Autowired
	private ModelMapper modelMapper;

	@GetMapping("/order/{orderId}")
	public ResponseEntity<SuccessResponseDto> getOrder(HttpServletRequest request,
			@PathVariable(name = "orderId") Long orderId) {
		User user = authenticationService.getAuthenticatedUser(authenticationService.getAuthentication());

		Order order = orderManager.getOrder(orderId, user);

		OrderDto orderDto = modelMapper.map(order, OrderDto.class);

		List<OrderItem> orderItems = orderManager.getOrderItems(order);

		List<OrderItemDto> orderItemDtos = orderItems.stream().map(e -> modelMapper.map(e, OrderItemDto.class))
				.collect(Collectors.toList());

		OrderDetailsDto orderDetailsDto = OrderDetailsDto.builder().order(orderDto).orderItems(orderItemDtos).build();

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Order details fetched successfully").data(orderDetailsDto).path(request.getServletPath())
				.build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}

	@GetMapping("/order")
	public ResponseEntity<SuccessResponseDto> getOrders(HttpServletRequest request,
			@RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
			@RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {
		User user = authenticationService.getAuthenticatedUser(authenticationService.getAuthentication());

		Pageable pageable = PageRequest.of(pageNumber, pageSize,
				Sort.by(Sort.Direction.valueOf("DESC"), "createdDate"));

		Page<Order> orderPage = orderManager.getOrders(user, pageable);

		List<OrderDto> ordoDtos = orderPage.getContent().stream().map(e -> modelMapper.map(e, OrderDto.class))
				.collect(Collectors.toList());

		Map<String, Object> pageDetails = new HashMap<>();
		pageDetails.put("orders", ordoDtos);
		pageDetails.put("currentPage", orderPage.getNumber());
		pageDetails.put("totalItems", orderPage.getTotalElements());
		pageDetails.put("totalPages", orderPage.getTotalPages());

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Orders fetched successfully").data(pageDetails).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}

	@PostMapping("/order")
	public ResponseEntity<SuccessResponseDto> createOrder(HttpServletRequest request,
			@RequestBody CreateOrderDto payload) {
		Set<ConstraintViolation<CreateOrderDto>> violations = validator.validate(payload);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException("Constraint violation", violations);
		}

		User user = authenticationService.getAuthenticatedUser(authenticationService.getAuthentication());

		Store store = storeService.getStore(payload.getStoreId());

		Order order = orderManager.createOrder(user, store);

		OrderDto orderDto = modelMapper.map(order, OrderDto.class);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Order created successfully").data(orderDto).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}
}
