create table movies.directors
(
    id            bigint auto_increment primary key ,
    first_name    varchar(30)  not null,
    last_name     varchar(30)  not null,
    date_of_birth DATE         not null,
    date_of_death DATE         null,
    biography     varchar(255) null,
    gender        varchar(10)  not null
);

create table movies.director_grade
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    director_id bigint not null,
    grade INT not null,
    CONSTRAINT fk_director FOREIGN KEY (director_id) references movies.directors(id) on delete cascade,
    user_id varchar(255) not null
);

create table movies.director_movies
(
    director_id bigint not null,
    movie_id bigint not null,
    primary key (director_id, movie_id),
    foreign key (director_id) references movies.directors(id),
    foreign key (movie_id) references movies.movie(id)
);

