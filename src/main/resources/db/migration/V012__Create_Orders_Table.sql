CREATE TABLE orders (
	id BIGINT primary key GENERATED ALWAYS AS IDENTITY,
	user_id BIGINT NOT NULL,
	store_id BIGINT NOT NULL,
		
	name varchar(50) NOT NULL,
	mobile varchar(50) NOT NULL,
	email varchar(50),
	
	state varchar(50) NOT NULL,
	city varchar(50) NOT NULL,
	pincode varchar(50) NOT NULL,
	line1 varchar(100) NOT NULL,
	line2 varchar(100),
	landmark varchar(50),
	
	sub_total INT NOT NULL,
	shipping_charges INT NOT NULL,
	total INT NOT NULL,
	
	status varchar(50) NOT NULL,
	
	created_by varchar(50),
	created_date timestamp,
	last_modified_by varchar(50),
	last_modified_date timestamp,
	
	CONSTRAINT fk_user_id FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE,
	CONSTRAINT fk_store_id FOREIGN KEY(store_id) REFERENCES stores(id) ON DELETE CASCADE
);