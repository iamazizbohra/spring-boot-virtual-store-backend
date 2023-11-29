package com.coedmaster.vstore.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartDto {
	private Long id;

	private Long storeId;

	private List<CartItemDto> cartItems;

	private Integer subTotal;

	private Integer shippingCharges;

	private Integer total;

	private AddressDto shippingAddress;
}
