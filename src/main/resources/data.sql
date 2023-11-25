insert into roles( id, name) values (1, 'ADMIN');
insert into roles( id, name) values (2, 'USER');
insert into users ( id, first_name, last_name, age, email, password) values (1, 'admin', 'admin', 40, 'admin@mail.ru', '$2a$10$IBFsAdZaGvOaGRxVgfL3kOh/27gk/hcsjPoEMgDDbsSQJwDViRkkS');
insert into user_role (user_id, role_id) values (1, 1);
insert into user_role (user_id, role_id) values (1, 2);
