CREATE TABLE order_items (
	id BIGINT primary key GENERATED ALWAYS AS IDENTITY,
	order_id BIGINT NOT NULL,		
	category_id BIGINT NOT NULL,
	product_id BIGINT NOT NULL,
	
	name varchar(50) NOT NULL,
	image varchar(255),
	price INT NOT NULL,
	quantity INT NOT NULL,
	
	created_by varchar(50),
	created_date timestamp,
	last_modified_by varchar(50),
	last_modified_date timestamp,
	
	CONSTRAINT fk_order_id FOREIGN KEY(order_id) REFERENCES orders(id) ON DELETE CASCADE,
	CONSTRAINT fk_category_id FOREIGN KEY(category_id) REFERENCES categories(id) ON DELETE CASCADE,
	CONSTRAINT fk_product_id FOREIGN KEY(product_id) REFERENCES products(id) ON DELETE CASCADE
);