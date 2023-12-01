package com.coedmaster.vstore.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class UserDto {
	private Long id;
	
	private UUID uuid;

	private String firstName;

	private String lastName;

	private String mobile;

	private String email;

	private String gender;
}
