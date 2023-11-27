package com.coedmaster.vstore.controller.buyer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coedmaster.vstore.dto.request.CartItemRequestDto;
import com.coedmaster.vstore.dto.response.CartItemResponseDto;
import com.coedmaster.vstore.dto.response.CartResponseDto;
import com.coedmaster.vstore.dto.response.SuccessResponseDto;
import com.coedmaster.vstore.exception.EntityNotFoundException;
import com.coedmaster.vstore.model.Cart;
import com.coedmaster.vstore.model.CartItem;
import com.coedmaster.vstore.model.Product;
import com.coedmaster.vstore.model.Store;
import com.coedmaster.vstore.model.User;
import com.coedmaster.vstore.service.AuthenticationService;
import com.coedmaster.vstore.service.CartItemService;
import com.coedmaster.vstore.service.CartService;
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
	private CartService cartService;

	@Autowired
	private CartItemService cartItemService;

	@Autowired
	private StoreService storeService;

	@Autowired
	private ProductService productService;

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

		Cart cart = cartService.getCart(user, store).orElseThrow(() -> new EntityNotFoundException("Cart not found"));

		List<CartItem> cartItems = cart.getCartItems();

		List<CartItemResponseDto> cartItemResponseDtos = cartItems.stream()
				.map(e -> modelMapper.map(e, CartItemResponseDto.class)).collect(Collectors.toList());

		CartResponseDto cartResponseDto = CartResponseDto.builder().cartId(cart.getId()).cartItems(cartItemResponseDtos)
				.build();

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Cart fetched successfully").data(cartResponseDto).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	};

	@PostMapping("/store/{storeId}/cart")
	public ResponseEntity<SuccessResponseDto> addItem(HttpServletRequest request,
			@PathVariable(name = "storeId") Long storeId, @RequestBody CartItemRequestDto payload) {
		Set<ConstraintViolation<CartItemRequestDto>> violations = validator.validate(payload);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException("Constraint violation", violations);
		}

		User user = authenticationService.getAuthenticatedUser(authenticationService.getAuthentication());

		Store store = storeService.getStoreById(storeId);

		Product product = productService.getProduct(payload.getProductId(), store);

		Cart cart;

		CartItem cartItem;

		Optional<Cart> cartOptional = cartService.getCart(user);
		if (cartOptional.isEmpty()) {
			cart = cartService.createCart(user, store);
		} else {
			cart = cartOptional.get();

			if (!cart.getStore().getId().equals(store.getId())) {
				cartService.deleteCart(user, cart.getStore());

				cart = cartService.createCart(user, store);
			}
		}

		Optional<CartItem> cartItemOptional = cartItemService.getCartItem(cart, product);
		if (cartItemOptional.isEmpty()) {
			cartItem = cartItemService.createCartItem(cart, product, 1);
		} else {
			cartItem = cartItemOptional.get();

			cartItem = cartItemService.updateCartItemQuantity(cart, product, payload.getQuantity());
		}

		CartItemResponseDto cartItemResponseDto = modelMapper.map(cartItem, CartItemResponseDto.class);

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Item added successfully").data(cartItemResponseDto).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}

	@DeleteMapping("/store/{storeId}/cart/{cartItemId}")
	public ResponseEntity<SuccessResponseDto> deleteItem(HttpServletRequest request,
			@PathVariable(name = "storeId") Long storeId, @PathVariable(name = "cartItemId") Long cartItemId) {
		User user = authenticationService.getAuthenticatedUser(authenticationService.getAuthentication());

		Store store = storeService.getStoreById(storeId);

		Cart cart;

		CartItem cartItem;

		Optional<Cart> cartOptional = cartService.getCart(user, store);
		if (cartOptional.isEmpty()) {
			throw new EntityNotFoundException("Cart not found");
		} else {
			cart = cartOptional.get();
		}

		Optional<CartItem> cartItemOptional = cartItemService.getCartItem(cartItemId, cart);
		if (cartItemOptional.isEmpty()) {
			throw new EntityNotFoundException("Cart item not found");
		} else {
			cartItem = cartItemOptional.get();
		}

		cartItemService.deleteCartItem(cart, cartItem.getProduct());

		List<CartItem> cartItems = cart.getCartItems();
		if (cartItems.size() == 0) {
			cartService.deleteCart(user, cart.getStore());
		}

		SuccessResponseDto successResponseDto = SuccessResponseDto.builder().timestamp(LocalDateTime.now()).status(200)
				.message("Item deleted successfully").data(null).path(request.getServletPath()).build();

		return new ResponseEntity<SuccessResponseDto>(successResponseDto, HttpStatus.OK);
	}
}
