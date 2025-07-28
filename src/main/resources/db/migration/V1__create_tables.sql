CREATE TABLE movie (
                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       title VARCHAR(255) NOT NULL,
                       year INT NOT NULL,
                       category VARCHAR(100),
                       description TEXT,
                       prizes VARCHAR(255)
);

CREATE TABLE movie_grade (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             movie_id BIGINT NOT NULL,
                             grade INT NOT NULL,
                             CONSTRAINT fk_movie FOREIGN KEY (movie_id) REFERENCES movie(id) ON DELETE CASCADE
);
