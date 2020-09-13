insert into roles (role_id,role) values (1,'ADMIN');
insert into roles (role_id,role) values (2,'USER');

insert into users (user_id, name, last_name,birth_date,user_name,about_me,address,active,password) values(1,'Admin','Admin','1998-12-12','admin','I am admin','Bishkek','true','admin');

insert into user_role (user_id,role_id) values(1,1);

insert into users (user_id, name, last_name,birth_date,user_name,about_me,address,active,password) values(2,'User','User','1998-12-12','user','I am user','Bishkek','true','user');

insert into user_role (user_id,role_id) values(2,2);