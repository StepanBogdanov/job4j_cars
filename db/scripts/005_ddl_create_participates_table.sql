CREATE TABLE IF NOT EXISTS participates
(
    id      SERIAL PRIMARY KEY,
    post_id INT REFERENCES auto_posts(id),
    user_id INT REFERENCES auto_users(id),
    UNIQUE (user_id, post_id)
);