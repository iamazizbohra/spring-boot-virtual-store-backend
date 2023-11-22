package com.coedmaster.vstore.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "stores")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Store {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;

	private String name;

	private String code;

	private String logo;

	private String mobile;

	private String whatsapp;

	private String email;

	private String latitude;

	private String longitude;

	private String address;

	private boolean enabled;

	@Column(updatable = false)
	@CreatedBy
	private String createdBy;

	@Column(updatable = false)
	@CreatedDate
	private LocalDateTime createdDate;

	@LastModifiedBy
	private String lastModifiedBy;

	@LastModifiedDate
	private LocalDateTime lastModifiedDate;
}
