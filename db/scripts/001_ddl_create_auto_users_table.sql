CREATE TABLE IF NOT EXISTS auto_users
(
    id          SERIAL PRIMARY KEY,
    login       varchar not null,
    password    varchar not null
);