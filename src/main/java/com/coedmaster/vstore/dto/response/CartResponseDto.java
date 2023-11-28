package com.coedmaster.vstore.dto.response;

import java.util.List;

import com.coedmaster.vstore.dto.AddressDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartResponseDto {
	private Long cartId;

	private List<CartItemResponseDto> cartItems;

	private Integer subTotal;

	private Integer shippingCharges;

	private Integer total;

	private AddressDto shippingAddress;
}
