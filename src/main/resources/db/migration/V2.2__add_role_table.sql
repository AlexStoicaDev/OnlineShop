drop table if exists role;
create  table role(

  id integer not null AUTO_INCREMENT,
  name VARCHAR (255),

   primary key (id)
);

drop table if exists customer_role;

 create table customer_role (
   customer_id integer not null,
   role_id    integer not null
 );

alter table customer_role
   add constraint FK_customer_customer_role foreign key (customer_id) references customer (id);
alter table customer_role
    add constraint FK_role_customer_role foreign key (role_id) references role (id);

insert into role (id,name) values(1,'ROLE_ADMIN');
insert into role(id,name) values(2,'ROLE_USER');

insert into customer (id,first_name,last_name,username,password) values(9999,'admin','admin','admin','$2a$10$UTdnj4KtVvhGR6p08XFqr.IDh5fZkAUrtRCcdFsoa4KzSWmEAy7V.');


insert into customer_role (customer_id, role_id) values (9999,1);
insert into customer_role (customer_id, role_id) values (9999,2);