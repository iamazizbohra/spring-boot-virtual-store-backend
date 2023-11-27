CREATE TABLE addresses (
	id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
	user_id BIGINT NOT NULL,
	title varchar(50) NOT NULL,
	name varchar(50) NOT NULL,
	mobile varchar(50) NOT NULL,
	state varchar(50) NOT NULL,
	city varchar(50) NOT NULL,
	pincode varchar(50) NOT NULL,
	line1 varchar(50) NOT NULL,
	line2 varchar(50),
	landmark varchar(50),
	is_default boolean,
	created_by varchar(50),
	created_date timestamp,
	last_modified_by varchar(50),
	last_modified_date timestamp,
	CONSTRAINT fk_user_id FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE
);