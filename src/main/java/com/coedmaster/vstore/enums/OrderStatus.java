package com.coedmaster.vstore.enums;

public enum OrderStatus {
	
	PENDING("PENDING"), ACCEPTED("ACCEPTED"), SHIPPED("SHIPPED"), DELIVERED("DELIVERED"), REJECTED("REJECTED"),
	CANCELLED("CANCELLED");

	OrderStatus(String string) {

	}
	
}
