package com.coedmaster.vstore.dto;

import lombok.Data;

@Data
public class OrderDto {

	private Long id;

	private Long userId;

	private Long storeId;

	private String name;

	private String mobile;

	private String email;

	private String state;

	private String city;

	private String pincode;

	private String line1;

	private String line2;

	private String landmark;

	private Integer subTotal;

	private Integer shippingCharges;

	private Integer total;

	private String status;

	private String createdDate;
}
