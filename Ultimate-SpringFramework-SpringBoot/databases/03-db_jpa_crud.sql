CREATE DATABASE db_jpa_crud;
use db_jpa_crud;

create table products(
	id BIGINT NOT NULL AUTO_INCREMENT,
	name VARCHAR(45),
	price INT,
	description TEXT,
	PRIMARY KEY (id)
);

select * from db_jpa_crud.products p;

alter table db_jpa_crud.products ADD column sku VARCHAR(45);

create table users(
	id BIGINT NOT NULL AUTO_INCREMENT,
	name VARCHAR(45),
	lastname VARCHAR(45),
	birthdate DATE,
	email VARCHAR(100),
	PRIMARY KEY (id)
);

select * from db_jpa_crud.users u;






