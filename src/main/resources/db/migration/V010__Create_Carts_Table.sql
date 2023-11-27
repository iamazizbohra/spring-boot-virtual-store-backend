CREATE TABLE carts (
	id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
	user_id BIGINT NOT NULL,
	store_id BIGINT NOT NULL,
	shipping_address_id BIGINT,
	created_by varchar(50),
	created_date timestamp,
	last_modified_by varchar(50),
	last_modified_date timestamp,
	CONSTRAINT fk_user_id FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE,
	CONSTRAINT fk_store_id FOREIGN KEY(store_id) REFERENCES stores(id) ON DELETE CASCADE,
	CONSTRAINT fk_shipping_address_id FOREIGN KEY(shipping_address_id) REFERENCES addresses(id) ON DELETE CASCADE
);