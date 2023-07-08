CREATE TABLE auto_users IF NOT EXISTS
(
    id          SERIAL PRIMARY KEY,
    login       varchar not null,
    password    varchar not null
);