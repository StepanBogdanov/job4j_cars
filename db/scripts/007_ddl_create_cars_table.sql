CREATE TABLE IF NOT EXISTS cars
(
    id          SERIAL PRIMARY KEY,
    name        TEXT not null,
    engine_id   INT not null REFERENCES engines(id)
);