package com.coedmaster.vstore.model;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.coedmaster.vstore.model.audit.AuditSection;
import com.coedmaster.vstore.model.audit.Auditable;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "order_items")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderItem implements Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "order_id", referencedColumnName = "id")
	private Order order;

	@OneToOne
	@JoinColumn(name = "category_id", referencedColumnName = "id")
	private Category category;

	@OneToOne
	@JoinColumn(name = "product_id", referencedColumnName = "id")
	private Product product;

	private String name;

	private String image;

	private Integer price;

	private Integer quantity;

	@Embedded
	@JsonIgnore
	private AuditSection auditSection = new AuditSection();

}
