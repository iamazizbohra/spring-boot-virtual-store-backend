package com.coedmaster.vstore.model;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.coedmaster.vstore.enums.OrderStatus;
import com.coedmaster.vstore.model.audit.AuditSection;
import com.coedmaster.vstore.model.audit.Auditable;
import com.coedmaster.vstore.model.converter.OrderStatusConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Order implements Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "store_id", referencedColumnName = "id")
	private Store store;

	private String name;

	private String mobile;

	private String email;

	private String state;

	private String city;

	private String pincode;

	private String line1;

	private String line2;

	private String landmark;

	private Integer subTotal;

	private Integer shippingCharges;

	private Integer total;

	@Convert(converter = OrderStatusConverter.class)
	private OrderStatus status;

	@Embedded
	@JsonIgnore
	private AuditSection auditSection = new AuditSection();

}
