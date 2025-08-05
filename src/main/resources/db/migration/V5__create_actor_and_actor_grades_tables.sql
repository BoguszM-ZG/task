create table movies.actors
(
    id              BIGINT auto_increment
        primary key,
    gender          varchar(10)   not null,
    first_name      varchar(30)  not null,
    last_name       varchar(30)  not null,
    age             int          null,
    date_of_birth   DATE         null,
    place_of_birth  VARCHAR(40)  null,
    height          int          null,
    biography       varchar(255) null,
    count_of_prizes int          null
);

create table movies.actor_grades
(
    id       BIGINT auto_increment
        primary key,
    actor_id BIGINT not null,
    grade    int    null,
    CONSTRAINT fk_actors FOREIGN KEY (actor_id) REFERENCES movies.actors(id) ON DELETE CASCADE
);