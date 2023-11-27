CREATE TABLE cart_items (
	id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
	cart_id BIGINT NOT NULL,
	product_id BIGINT NOT NULL,
	name varchar(50) NOT NULL,
	image varchar(255),
	price INT NOT NULL,
	old_price INT,
	quantity INT NOT NULL,
	created_by varchar(50),
	created_date timestamp,
	last_modified_by varchar(50),
	last_modified_date timestamp,
	CONSTRAINT fk_cart_id FOREIGN KEY(cart_id) REFERENCES carts(id) ON DELETE CASCADE,
	CONSTRAINT fk_product_id FOREIGN KEY(product_id) REFERENCES products(id) ON DELETE CASCADE
);