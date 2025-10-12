USE db_springboot;

INSERT INTO db_springboot.persons
(name, last_name, programming_language)
VALUES(
'Ana', 'Peralta', 'Java'),
('Sol', 'Segarra', 'JavaScript'),
('Oswaldinho', 'Bonilla', 'Python');

INSERT INTO db_springboot.persons
(name, last_name, programming_language)
VALUES(
'Diego', 'Peralta', 'Rust'),
('Nube', 'Peralta', 'Go'),
('Karen', 'Peralta', 'Java');

select * from db_springboot.persons;

alter table db_springboot.persons add created_at datetime;
alter table db_springboot.persons add updated_at datetime;
