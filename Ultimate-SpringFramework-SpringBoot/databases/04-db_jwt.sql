CREATE DATABASE db_jwt;
use db_jwt;

create table users(
	id BIGINT NOT NULL AUTO_INCREMENT,
	username VARCHAR(18) NOT NULL,
	password VARCHAR(60) NOT NULL,
	enable BOOLEAN NOT NULL,
	PRIMARY KEY (id)
);

ALTER TABLE db_jwt.users
ADD UNIQUE INDEX username_UNIQUE (username ASC) VISIBLE;

create table roles(
	id BIGINT NOT NULL AUTO_INCREMENT,
	name VARCHAR(45) NOT NULL,
	PRIMARY KEY (id)
)

ALTER TABLE db_jwt.roles
ADD UNIQUE INDEX name_UNIQUE (name ASC) VISIBLE;


create table users_roles(
	user_id BIGINT NOT NULL,
	role_id BIGINT NOT NULL,
	PRIMARY KEY (user_id, role_id)
)

ALTER TABLE db_jwt.users_roles 
ADD CONSTRAINT FK_users
FOREIGN KEY (user_id)
REFERENCES db_jwt.users (id);

ALTER TABLE db_jwt.users_roles 
ADD CONSTRAINT FK_roles
FOREIGN KEY (role_id)
REFERENCES db_jwt.roles (id);



INSERT INTO db_jwt.roles (name) VALUES ('ROLE_ADMIN');
INSERT INTO db_jwt.roles (name) VALUES ('ROLE_USER');

create table products(
	id BIGINT NOT NULL AUTO_INCREMENT,
	name VARCHAR(45),
	price INT,
	description TEXT,
	PRIMARY KEY (id)
);

alter table db_jwt.products ADD column sku VARCHAR(45);

select * from db_jwt.roles r ;
select * from db_jwt.users u ;
select * from db_jwt.products p ;

INSERT INTO db_jwt.products (name, price, description, sku) VALUES('Manicho', 2, 'Manicho grande', '123');
INSERT INTO db_jwt.products (name, price, description, sku) VALUES('Doritos', 2, 'Doritos de quesso', '456');
INSERT INTO db_jwt.products (name, price, description, sku) VALUES('Caramelos', 1, 'Funda de caramelos grande', '789');
INSERT INTO db_jwt.products (name, price, description, sku) VALUES('Coca Cola', 3, 'Coca Cola grande', '101');




