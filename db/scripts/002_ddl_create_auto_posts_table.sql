CREATE TABLE IF NOT EXISTS auto_posts
(
    id              SERIAL PRIMARY KEY,
    description     varchar not null,
    created         timestamp now(),
    auto_user_id    int REFERENCES auto_users(id)
);