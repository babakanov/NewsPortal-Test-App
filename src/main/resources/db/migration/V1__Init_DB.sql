drop table if exists news_portal cascade;
drop table if exists roles cascade;
drop table if exists user_role cascade;
drop table if exists users cascade;

drop sequence if exists hibernate_sequence;


create sequence hibernate_sequence start 1 increment 1;

create table news_portal (
	id int8 generated by default as identity,
	date_of_create date,
	description varchar(255),
	heading varchar(255),
	image text,
	user_id int8, primary key (id)
	);
create table roles (
	role_id int8 not null,
	role varchar(255),
	primary key (role_id)
	);
create table user_role (
	user_id int8 not null,
	role_id int8 not null,
	primary key (user_id, role_id)
	);
create table users (
	user_id int8 not null,
	about_me varchar(255),
	active boolean,
	address varchar(255),
	birth_date date not null,
	last_name varchar(255),
	name varchar(255), password varchar(255),
	user_name varchar(255), primary key (user_id)
	);


alter table if exists news_portal add constraint news_portal_fk foreign key (user_id) references users;
alter table if exists user_role add constraint user_role_fk foreign key (role_id) references roles;