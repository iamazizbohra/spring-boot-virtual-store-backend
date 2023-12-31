CREATE TABLE banners (
	id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
	store_id BIGINT NOT NULL,
	title varchar(50) NOT NULL,
	image varchar(255),
	sort_order SMALLINT DEFAULT 0,
	enabled boolean,
	created_by varchar(50),
	created_date timestamp,
	last_modified_by varchar(50),
	last_modified_date timestamp,
	CONSTRAINT fk_store_id FOREIGN KEY(store_id) REFERENCES stores(id) ON DELETE CASCADE
);