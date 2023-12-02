package com.coedmaster.vstore.domain.converter;

import java.util.stream.Stream;

import com.coedmaster.vstore.enums.Gender;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class GenderConverter implements AttributeConverter<Gender, String> {

	@Override
	public String convertToDatabaseColumn(Gender attribute) {
		if (attribute == null)
			return null;

		return attribute.name();
	}

	@Override
	public Gender convertToEntityAttribute(String dbData) {
		if (dbData == null)
			return null;

		return Stream.of(Gender.values()).filter(e -> e.name().equals(dbData)).findFirst()
				.orElseThrow(IllegalArgumentException::new);
	}

}
