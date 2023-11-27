CREATE TABLE products (
	id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
	store_id BIGINT NOT NULL,
	category_id BIGINT NOT NULL,
	name varchar(50) NOT NULL,
	description varchar,
	image varchar(255),
	price INT NOT NULL,
	old_price INT,
	quantity INT NOT NULL,
	enabled boolean,
	created_by varchar(50),
	created_date timestamp,
	last_modified_by varchar(50),
	last_modified_date timestamp,
	CONSTRAINT fk_store_id FOREIGN KEY(store_id) REFERENCES stores(id) ON DELETE CASCADE,
	CONSTRAINT fk_category_id FOREIGN KEY(category_id) REFERENCES categories(id) ON DELETE CASCADE
);