package com.coedmaster.vstore.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartResponseDto {
	private Long cartId;
	
	private List<CartItemResponseDto> cartItems;
}
