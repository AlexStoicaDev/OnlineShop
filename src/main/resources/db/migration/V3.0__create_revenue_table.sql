create table revenue (
  id  integer not null AUTO_INCREMENT,
  date date,
  sum decimal(19, 2),
  location_id integer,
  primary key (id)
);

alter table revenue
  add constraint FK_location_revenue foreign key (location_id) references location (id);