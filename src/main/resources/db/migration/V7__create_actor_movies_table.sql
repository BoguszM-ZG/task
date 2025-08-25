CREATE TABLE movies.actor_movies (
                              actor_id BIGINT NOT NULL,
                              movie_id BIGINT NOT NULL,
                              PRIMARY KEY (actor_id, movie_id),
                              FOREIGN KEY (actor_id) REFERENCES movies.actors(id),
                              FOREIGN KEY (movie_id) REFERENCES movies.movie(id)
);
