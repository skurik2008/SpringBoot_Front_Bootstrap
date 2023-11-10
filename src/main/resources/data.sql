insert into roles( id, name) values (1, 'ADMIN');
insert into roles( id, name) values (2, 'USER');
insert into users ( id, login, password) values (1, 'admin', '$2a$10$IBFsAdZaGvOaGRxVgfL3kOh/27gk/hcsjPoEMgDDbsSQJwDViRkkS');
insert into user_role (user_id, role_id) values (1, 1);