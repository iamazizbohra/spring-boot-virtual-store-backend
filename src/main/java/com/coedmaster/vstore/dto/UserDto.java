package com.coedmaster.vstore.dto;

import lombok.Data;

@Data
public class UserDto {
	private Long id;

	private String firstName;

	private String lastName;

	private String mobile;

	private String email;

	private String gender;
}
