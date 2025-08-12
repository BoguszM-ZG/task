create table movies.user_alert
(
    id      BIGINT auto_increment
        primary key,
    user_id varchar(255)          not null,
    message varchar(255)          null,
    `read`  boolean default FALSE not null,
    created_at DATETIME default CURRENT_TIMESTAMP not null
);

