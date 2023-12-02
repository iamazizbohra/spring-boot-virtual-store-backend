package com.coedmaster.vstore.domain.converter;

import java.util.stream.Stream;

import com.coedmaster.vstore.enums.OrderStatus;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class OrderStatusConverter implements AttributeConverter<OrderStatus, String> {
	
	@Override
	public String convertToDatabaseColumn(OrderStatus attribute) {
		if (attribute == null)
			return null;

		return attribute.name();
	}

	@Override
	public OrderStatus convertToEntityAttribute(String dbData) {
		if (dbData == null)
			return null;

		return Stream.of(OrderStatus.values()).filter(e -> e.name().equals(dbData)).findFirst()
				.orElseThrow(IllegalArgumentException::new);
	}
}
