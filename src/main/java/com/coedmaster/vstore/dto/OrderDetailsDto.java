package com.coedmaster.vstore.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderDetailsDto {
	
	private OrderDto order;

	private List<OrderItemDto> orderItems;
	
}
