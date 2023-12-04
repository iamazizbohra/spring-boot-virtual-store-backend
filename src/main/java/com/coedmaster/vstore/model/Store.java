package com.coedmaster.vstore.model;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.coedmaster.vstore.model.audit.AuditSection;
import com.coedmaster.vstore.model.audit.Auditable;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "stores")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Store implements Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;

	private String name;

	@Column(unique = true)
	private String code;

	private String logo;

	private String mobile;

	private String whatsapp;

	private String email;

	private String latitude;

	private String longitude;

	private String address;

	private boolean enabled;

	@Embedded
	@JsonIgnore
	private AuditSection auditSection = new AuditSection();

}
