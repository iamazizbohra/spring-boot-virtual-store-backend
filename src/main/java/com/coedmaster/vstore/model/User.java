package com.coedmaster.vstore.model;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.coedmaster.vstore.enums.Gender;
import com.coedmaster.vstore.enums.UserType;
import com.coedmaster.vstore.model.audit.AuditSection;
import com.coedmaster.vstore.model.audit.Auditable;
import com.coedmaster.vstore.model.converter.GenderConverter;
import com.coedmaster.vstore.model.converter.UserTypeConverter;
import com.coedmaster.vstore.model.embeddable.FullName;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User implements Auditable {

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
	private List<Role> roles;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, updatable = false)
	private UUID uuid;

	@Column(updatable = false)
	@Convert(converter = UserTypeConverter.class)
	private UserType userType;

	@Embedded
	private FullName fullName;

	private String mobile;

	@JsonIgnore
	private String password;

	private String email;

	@Convert(converter = GenderConverter.class)
	private Gender gender;

	private boolean enabled;

	@Embedded
	@JsonIgnore
	private AuditSection auditSection = new AuditSection();

}
