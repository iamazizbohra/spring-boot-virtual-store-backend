package com.coedmaster.vstore.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartTotalSummary {
	
	private Integer subTotal;

	private Integer shippingCharges;

	private Integer total;
}
