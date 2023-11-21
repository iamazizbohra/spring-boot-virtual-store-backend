CREATE TABLE users_roles (
	id BIGINT primary key GENERATED ALWAYS AS IDENTITY,
	user_id BIGINT NOT NULL,
	role_id BIGINT NOT NULL,
	CONSTRAINT fk_customer FOREIGN KEY(user_id) REFERENCES users(id),
	CONSTRAINT fk_role FOREIGN KEY(role_id) REFERENCES roles(id)
);