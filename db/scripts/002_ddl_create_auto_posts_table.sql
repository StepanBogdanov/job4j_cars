CREATE TABLE auto_posts
(
    id              SERIAL PRIMARY KEY,
    description     varchar not null,
    created         timestamp,
    auto_user_id    int REFERENCES auto_users(id)
);