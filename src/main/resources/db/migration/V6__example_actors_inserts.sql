INSERT INTO movies.actors (gender, first_name, last_name, age, date_of_birth, place_of_birth, height, biography,
                           count_of_prizes)
VALUES ('male', 'Robert', 'Downey', 59, '1965-04-04', 'New York, USA', 174, 'Known for Iron Man.', 12),
       ('female', 'Scarlett', 'Johansson', 40, '1984-11-22', 'New York, USA', 160, 'Known for Black Widow.', 8),
       ('male', 'Leonardo', 'DiCaprio', 50, '1974-11-11', 'Los Angeles, USA', 183, 'Oscar-winning actor.', 15),
       ('female', 'Jennifer', 'Lawrence', 35, '1990-08-15', 'Louisville, USA', 175, 'Star of Hunger Games.', 9),
       ('male', 'Brad', 'Pitt', 61, '1963-12-18', 'Shawnee, USA', 180, 'Famous Hollywood actor.', 10),
       ('female', 'Emma', 'Stone', 36, '1989-11-06', 'Scottsdale, USA', 168, 'La La Land actress.', 7),
       ('male', 'Tom', 'Hanks', 69, '1956-07-09', 'Concord, USA', 183, 'Legendary actor.', 20),
       ('female', 'Natalie', 'Portman', 44, '1981-06-09', 'Jerusalem, Israel', 160, 'Known for Black Swan.', 11),
       ('male', 'Chris', 'Hemsworth', 42, '1983-08-11', 'Melbourne, Australia', 190, 'Played Thor.', 5),
       ('female', 'Meryl', 'Streep', 76, '1949-06-22', 'Summit, USA', 168, 'Most-nominated actress.', 25);


INSERT INTO movies.actor_grades (actor_id, grade)
VALUES (1, 9),
       (2, 8),
       (3, 10),
       (4, 9),
       (5, 8),
       (6, 7),
       (7, 10),
       (8, 9),
       (9, 8),
       (10, 10);
