package com.coedmaster.vstore.controller.buyer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coedmaster.vstore.dto.AddressDto;
import com.coedmaster.vstore.dto.UpdateCartShippingAddressDto;
import com.coedmaster.vstore.dto.request.CartItemRequestDto;
import com.coedmaster.vstore.dto.response.CartItemResponseDto;
import com.coedmaster.vstore.dto.response.CartResponseDto;
import com.coedmaster.vstore.dto.response.SuccessResponseDto;
import com.coedmaster.vstore.exception.EntityNotFoundException;
import com.coedmaster.vstore.model.Address;
import com.coedmaster.vstore.model.Cart;
import com.coedmaster.vstore.model.CartItem;
import com.coedmaster.vstore.model.Product;
import com.coedmaster.vstore.model.Store;
import com.coedmaster.vstore.model.User;
import com.coedmaster.vstore.service.AddressService;
import com.coedmaster.vstore.service.AuthenticationService;
import com.coedmaster.vstore.service.CartManager;
import com.coedmaster.vstore.service.ProductService;
import com.coedmaster.vstore.service.StoreService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

@RestController
@RequestMapping("/buyer")
public class CartController {

	@Autowired
	private CartManager cartManager;

	@Autowired
	private StoreService storeService;

	@Autowired
	private ProductService productService;

	@Autowired
	private AddressService addressService;

	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private Validator validator;

	@GetMapping("/store/{storeId}/cart")
	public ResponseEntity<SuccessResponseDto> getCart(HttpServletRequest request,
			@PathVariable(name = "storeId") Long storeId) {
		User user = authenticationService.getAuthenticatedUser(authenticationService.getAuthentication());

		Store store = storeService.getStoreById(storeId);

		Cart cart = cartManager.getCart(user, store).orElseThrow(() -> new EntityNotFoundException("Cart not found"));

		List<CartItem> cartItems = cart.getCartItems();

		Address shippingAddress = cart.getShippingAddress();

		Integer subTotal = cartManager.calculateSubTotal(cart);
		Integer shippingCharges = 0;
		Integer total = subTotal + shippingCharges;

		List<CartItemResponseDto> cartItemResponseDtos = cartItems.stream()
				.map(e -> modelMapper.map(e, CartItemResponseDto.class)).collect(Collectors.toList());

		AddressDto addressDto = ObjectUtils.isNotEmpty(shippingAddress)
				? modelMapper.map(shippingAddress, AddressDto.class)
				: null;

		CartResponseDto cartResponseDto = CartResponseDto.builder().cartId(cart.getId()).subTotal(subTotal)
				.shippingCharges(shippingCharges).total(total).cartItems(cartItemResponseDtos)
				.shippingAddress(addressDto).build();

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Cart fetched successfully").data(cartResponseDto).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	};

	@PostMapping("/store/{storeId}/cart/item")
	public ResponseEntity<SuccessResponseDto> addCartItem(HttpServletRequest request,
			@PathVariable(name = "storeId") Long storeId, @RequestBody CartItemRequestDto payload) {
		Set<ConstraintViolation<CartItemRequestDto>> violations = validator.validate(payload);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException("Constraint violation", violations);
		}

		User user = authenticationService.getAuthenticatedUser(authenticationService.getAuthentication());

		Store store = storeService.getStoreById(storeId);

		Product product = productService.getProduct(payload.getProductId(), store);

		CartItem cartItem = cartManager.addCartItem(user, store, product, payload.getQuantity());

		CartItemResponseDto cartItemResponseDto = modelMapper.map(cartItem, CartItemResponseDto.class);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Item added successfully").data(cartItemResponseDto).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}

	@DeleteMapping("/store/{storeId}/cart/item/{cartItemId}")
	public ResponseEntity<SuccessResponseDto> deleteCartItem(HttpServletRequest request,
			@PathVariable(name = "storeId") Long storeId, @PathVariable(name = "cartItemId") Long cartItemId) {
		User user = authenticationService.getAuthenticatedUser(authenticationService.getAuthentication());

		Store store = storeService.getStoreById(storeId);

		cartManager.removeCartItem(cartItemId, user, store);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Item deleted successfully").data(null).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}

	@PostMapping("/store/{storeId}/cart/shippingaddress")
	public ResponseEntity<SuccessResponseDto> addCartShippingAddress(HttpServletRequest request,
			@PathVariable(name = "storeId") Long storeId, @RequestBody AddressDto payload) {
		Set<ConstraintViolation<AddressDto>> violations = validator.validate(payload);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException("Constraint violation", violations);
		}

		User user = authenticationService.getAuthenticatedUser(authenticationService.getAuthentication());

		Store store = storeService.getStoreById(storeId);

		Address address = addressService.createAddress(user, payload);

		cartManager.updateCartShippingAddress(user, store, address);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Shipping address added successfully").data(null).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}

	@PatchMapping("/store/{storeId}/cart/shippingaddress")
	public ResponseEntity<SuccessResponseDto> updateCartShippingAddress(HttpServletRequest request,
			@PathVariable(name = "storeId") Long storeId, @RequestBody UpdateCartShippingAddressDto payload) {
		Set<ConstraintViolation<UpdateCartShippingAddressDto>> violations = validator.validate(payload);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException("Constraint violation", violations);
		}

		User user = authenticationService.getAuthenticatedUser(authenticationService.getAuthentication());

		Store store = storeService.getStoreById(storeId);

		Address address = addressService.getAddress(payload.getId(), user);

		cartManager.updateCartShippingAddress(user, store, address);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Shipping address updated successfully").data(null).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}
}
