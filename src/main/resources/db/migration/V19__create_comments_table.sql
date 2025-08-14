create table movies.comments
(
    id       BIGINT auto_increment
        primary key,
    user_id varchar(255) not null,
    movie_id BIGINT not null REFERENCES movies.movie(id) ON DELETE CASCADE,
    comment_text varchar(500) not null,
    created_at DATETIME default CURRENT_TIMESTAMP not null
)


