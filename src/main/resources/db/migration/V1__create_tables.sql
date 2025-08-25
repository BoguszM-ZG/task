CREATE SCHEMA IF NOT EXISTS movies;


CREATE TABLE movies.movie (
                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       title VARCHAR(255) NOT NULL,
                       movie_year INT NOT NULL,
                       category VARCHAR(100),
                       description TEXT,
                       prizes VARCHAR(255)
);

CREATE TABLE movies.movie_grade
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    movie_id BIGINT NOT NULL,
    grade    INT    NOT NULL,
    CONSTRAINT fk_movie FOREIGN KEY (movie_id) REFERENCES movies.movie (id) ON DELETE CASCADE,
    user_id varchar(255) not null,
    created_at TIMESTAMP default now() not null
);