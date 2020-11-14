CREATE TYPE PRIVILEGE_NAME AS ENUM (
    'MANAGE_OWN_ACCOUNT',
    'MANAGE_ANY_ACCOUNT',
    'SEARCH_ANY_ACCOUNTS',
    'SUSPEND_RESTORE_ANY_ACCOUNT',

    'TASK_MANAGEMENT');

CREATE TYPE ROLE_NAME AS ENUM (
    'USER',
    'ADMIN');

CREATE TYPE PLUGIN_NAME AS ENUM (
    'USER',
    'ADMIN');

CREATE TABLE PRIVILEGE
(
    id   BIGSERIAL PRIMARY KEY,
    name PRIVILEGE_NAME NOT NULL UNIQUE
);

CREATE TABLE ROLE
(
    id   BIGSERIAL PRIMARY KEY,
    name ROLE_NAME NOT NULL UNIQUE
);

CREATE TABLE ROLE_PRIVILEGE
(
    role_id      BIGINT NOT NULL,
    privilege_id BIGINT NOT NULL,

    FOREIGN KEY (role_id) REFERENCES ROLE (id),
    FOREIGN KEY (privilege_id) REFERENCES PRIVILEGE (id)

);

CREATE TABLE "user"
(
    id                     BIGSERIAL PRIMARY KEY,
    identifier             UUID         NOT NULL UNIQUE,
    name                   VARCHAR(100) NOT NULL,
    email                  VARCHAR(100) NOT NULL UNIQUE,
    password               VARCHAR(100) NOT NULL,
    create_an_account_time TIMESTAMP WITHOUT TIME ZONE,
    active                 BOOLEAN
);

CREATE TABLE USER_ROLE
(
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,

    FOREIGN KEY (user_id) REFERENCES "user" (id),
    FOREIGN KEY (role_id) REFERENCES ROLE (id)
);

CREATE TABLE USER_PRIVILEGE
(
    user_id      BIGINT NOT NULL,
    privilege_id BIGINT NOT NULL,

    FOREIGN KEY (user_id) REFERENCES "user" (id),
    FOREIGN KEY (privilege_id) REFERENCES ROLE (id)
);

CREATE TABLE TOKEN
(
    id        BIGSERIAL PRIMARY KEY,
    activator UUID   NOT NULL UNIQUE,
    user_id   BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES "user" (id)
);


CREATE TABLE MOTION_DETECT
(
    id                BIGSERIAL PRIMARY KEY,
    identifier_user   UUID        NOT NULL,
    identifier_client UUID        NOT NULL,
    plugin            PLUGIN_NAME NOT NULL
);

CREATE TABLE RECORDING_CAMERA
(
    id                BIGSERIAL PRIMARY KEY,
    identifier_user   UUID        NOT NULL,
    identifier_client UUID        NOT NULL,
    plugin            PLUGIN_NAME NOT NULL,
    activate_time     TIME,
    deactivate_time   TIME
);
