CREATE TABLE price_history IF NOT EXISTS
(
   id               SERIAL PRIMARY KEY,
   before           BIGINT not null,
   after            BIGINT not null,
   created          TIMESTAMP WITHOUT TIME ZONE DEFAULT now(),
   auto_post_id     INT REFERENCES auto_posts(id)
);
