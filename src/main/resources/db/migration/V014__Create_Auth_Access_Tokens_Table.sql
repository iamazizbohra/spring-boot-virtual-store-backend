CREATE TABLE auth_access_tokens (
	id BIGINT primary key GENERATED ALWAYS AS IDENTITY,
	user_id BIGINT NOT NULL,
	name varchar(50) NOT NULL,
	token varchar NOT NULL,
	created_by varchar(50),
	created_date timestamp,
	last_modified_by varchar(50),
	last_modified_date timestamp,
	CONSTRAINT fk_user_id FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE
);