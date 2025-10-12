create database db_jpa_relationship;
use db_jpa_relationship;

select * from db_jpa_relationship.clients c ;

select * from db_jpa_relationship.invoices i ;

select * from db_jpa_relationship.addresses a ;

select * from db_jpa_relationship.clients_details cd ;

SELECT * from db_jpa_relationship.tbl_clientes_to_direcciones tctd ;


select c1_0.id, a1_0.id_cliente, a1_1.id, a1_1.number, a1_1.street,
       i1_0.client_id, i1_0.id, i1_0.description, i1_0.total,
       c1_0.lastname, c1_0.name
from db_jpa_relationship.clients c1_0
left join db_jpa_relationship.tbl_clientes_to_direcciones a1_0 on c1_0.id = a1_0.id_cliente
left join db_jpa_relationship.addresses a1_1 on a1_1.id = a1_0.id_direccion
left join db_jpa_relationship.invoices i1_0 on c1_0.id = i1_0.client_id
where c1_0.id = 1;

select * from db_jpa_relationship.students s ;

select * from db_jpa_relationship.courses c ;

select * from db_jpa_relationship.tbl_students_courses tsc ;