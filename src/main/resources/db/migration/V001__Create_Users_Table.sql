CREATE TABLE users (
	id BIGINT primary key GENERATED ALWAYS AS IDENTITY,
	user_type varchar(50) NOT NULL,
	first_name varchar(50) NOT NULL,
	last_name varchar(50) NOT NULL,
	mobile varchar(50) NOT NULL,
	password varchar(255) NOT NULL,
	email varchar(50),
	gender varchar(50) NOT NULL,
	enabled boolean NOT NULL,
	created_by varchar(50),
	created_date timestamp,
	last_modified_by varchar(50),
	last_modified_date timestamp
);