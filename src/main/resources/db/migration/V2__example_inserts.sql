INSERT INTO movie (id, title, year, category, description, prizes)
VALUES (2, 'The Godfather', 1972, 'Crime', 'The aging patriarch...', 'Oscar'),
       (3, 'Pulp Fiction', 1994, 'Crime', 'The lives of two mob hitmen...', 'Oscar'),
       (4, 'The Shawshank Redemption', 1994, 'Drama', 'Two imprisoned men bond...', 'Oscar'),
       (5, 'The Dark Knight', 2008, 'Action', 'Batman faces the Joker...', 'Oscar'),
       (6, 'Forrest Gump', 1994, 'Drama', 'The presidencies of Kennedy...', 'Oscar'),
       (7, 'The Matrix', 1999, 'Sci-Fi', 'A computer hacker learns...', 'Oscar'),
       (8, 'Fight Club', 1999, 'Drama', 'An insomniac and a soap maker...', 'Oscar'),
       (9, 'Interstellar', 2014, 'Sci-Fi', 'A team travels through a wormhole...', 'Oscar'),
       (10, 'Gladiator', 2000, 'Action', 'A former Roman General...', 'Oscar');


INSERT INTO movie_grade (movie_id, grade)
VALUES (1, 9),
       (1, 8),
       (2, 10),
       (3, 9),
       (4, 10),
       (5, 9),
       (6, 8),
       (7, 9),
       (8, 7),
       (9, 8);
