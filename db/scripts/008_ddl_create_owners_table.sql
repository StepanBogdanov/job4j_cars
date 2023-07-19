CREATE TABLE IF NOT EXISTS owners
(
    id      SERIAL PRIMARY KEY,
    name    TEXT,
    user_id INT not null REFERENCES auto_users(id)
);