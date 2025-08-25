CREATE TABLE movies.report_comment
(
    id         BIGINT auto_increment
        primary key,
    user_id    varchar(255)                       not null,
    comment_id BIGINT                             not null REFERENCES movies.comments (id) ON DELETE CASCADE,
    reason     varchar(500)                       ,
    created_at DATETIME default CURRENT_TIMESTAMP not null
)