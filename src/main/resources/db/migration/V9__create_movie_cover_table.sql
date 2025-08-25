CREATE TABLE movies.movie_cover (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    image_data LONGBLOB NOT NULL,
                                    movie_id BIGINT NOT NULL,
                                    CONSTRAINT fk_movie_cover_movie FOREIGN KEY (movie_id) REFERENCES movies.movie(id) ON DELETE CASCADE
);
