package com.coedmaster.vstore.domain.embeddable;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FullName {
	
	private String firstName;

	private String lastName;
}
