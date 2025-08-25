CREATE TABLE movies.movie_trailer (
                                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                      video_data LONGBLOB NOT NULL,
                                      file_name VARCHAR(255),
                                      content_type VARCHAR(100),
                                      movie_id BIGINT NOT NULL,
                                      CONSTRAINT fk_movie_trailer_movie FOREIGN KEY (movie_id) REFERENCES movies.movie(id) ON DELETE CASCADE
);
