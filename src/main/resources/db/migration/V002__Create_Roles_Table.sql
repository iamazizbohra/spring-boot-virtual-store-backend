CREATE TABLE roles (
	id BIGINT primary key GENERATED ALWAYS AS IDENTITY,
	name varchar(50) NOT NULL,
	created_by varchar(50),
	created_date timestamp,
	last_modified_by varchar(50),
	last_modified_date timestamp
);