package com.coedmaster.vstore.model.converter;

import java.util.stream.Stream;

import com.coedmaster.vstore.enums.UserType;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class UserTypeConverter implements AttributeConverter<UserType, String> {

	@Override
	public String convertToDatabaseColumn(UserType attribute) {
		if (attribute == null)
			return null;

		return attribute.name();
	}

	@Override
	public UserType convertToEntityAttribute(String dbData) {
		if (dbData == null)
			return null;

		return Stream.of(UserType.values()).filter(e -> e.name().equals(dbData)).findFirst()
				.orElseThrow(IllegalArgumentException::new);
	}

}
