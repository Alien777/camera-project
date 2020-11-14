INSERT INTO privilege (id, name)
VALUES (1, 'MANAGE_OWN_ACCOUNT');

INSERT INTO privilege (id, name)
VALUES (2, 'MANAGE_ANY_ACCOUNT');

INSERT INTO privilege (id, name)
VALUES (3, 'SEARCH_ANY_ACCOUNTS');

INSERT INTO privilege (id, name)
VALUES (4, 'SUSPEND_RESTORE_ANY_ACCOUNT');

INSERT INTO privilege (id, name)
VALUES (5, 'TASK_MANAGEMENT');


ALTER SEQUENCE privilege_id_seq RESTART WITH 6;

INSERT INTO role (id, name)
VALUES (1, 'ADMIN');

INSERT INTO role_privilege (role_id, privilege_id)
VALUES (1, 2);

INSERT INTO role_privilege (role_id, privilege_id)
VALUES (1, 3);

INSERT INTO role_privilege (role_id, privilege_id)
VALUES (1, 4);

INSERT INTO role_privilege (role_id, privilege_id)
VALUES (1, 5);

INSERT INTO role (id, name)
VALUES (2, 'USER');

INSERT INTO role_privilege (role_id, privilege_id)
VALUES (2, 1);

INSERT INTO role_privilege (role_id, privilege_id)
VALUES (2, 5);

INSERT INTO "user" (id, identifier, name, email, active, password)
VALUES (1, '40e6215d-b5c6-4896-987c-f30f3678f608', 'admin', 'adam@localhost.pl', true,
        '$2a$10$enUJc/4b.p7i9DwcDWu4S.RL5BCCk5I4ebz3O7gc/8HDuMZzjC57a');

ALTER SEQUENCE user_id_seq RESTART WITH 2;

INSERT INTO user_role (user_id, role_id)
VALUES (1, 1);
