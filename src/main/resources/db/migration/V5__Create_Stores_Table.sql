CREATE TABLE stores (
	id BIGINT primary key GENERATED ALWAYS AS IDENTITY,
	user_id BIGINT NOT NULL UNIQUE,
	name varchar(50) NOT NULL,
	code varchar(50) NOT NULL UNIQUE,
	logo varchar(255),
	mobile varchar(50),
	whatsapp varchar(50),
	email varchar(50),
	latitude varchar(50),
	longitude varchar(50),
	address varchar(255),
	enabled boolean,
	created_by varchar(50),
	created_date timestamp,
	last_modified_by varchar(50),
	last_modified_date timestamp,
	CONSTRAINT fk_user_id FOREIGN KEY(user_id) REFERENCES users(id)
);