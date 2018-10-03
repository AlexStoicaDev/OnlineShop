drop table if exists role;
create  table role(

  id integer not null,
  name VARCHAR (5),

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

insert into role (id,name) values(1,'ADM');
insert into role(id,name) values(2,'BASIC');