create table movies.favourite_movies
(
    id       BIGINT auto_increment
        primary key,
    user_id  varchar(255) not null,
    movie_id BIGINT not null REFERENCES movies.movie(id) ON DELETE CASCADE ,
    CONSTRAINT uq_user_movie UNIQUE (user_id, movie_id)
);

