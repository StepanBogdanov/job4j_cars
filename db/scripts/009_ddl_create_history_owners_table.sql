CREATE TABLE IF NOT EXISTS history_owners
(
    id SERIAL PRIMARY KEY,
    car_id INT not null REFERENCES cars(id),
    owner_id INT not null REFERENCES owners(id)
);