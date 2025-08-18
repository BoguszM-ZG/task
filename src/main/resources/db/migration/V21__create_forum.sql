create table movies.forum
(
    id         bigint auto_increment primary key,
    forum_name varchar(1000) not null
);

create table movies.forum_threads
(
    id          bigint auto_increment primary key,
    thread_name varchar(100) not null,
    forum_id    bigint not null,
    foreign key (forum_id) references movies.forum(id) on delete cascade
);

create table movies.messages
(
    id        bigint auto_increment primary key,
    thread_id bigint not null,
    content   varchar(500),
    user_id   varchar(255) not null,
    foreign key (thread_id) references movies.forum_threads(id) on delete cascade
);

create table movies.forum_members
(
    id       bigint auto_increment primary key,
    user_id  varchar(255) not null,
    forum_id bigint not null,
    foreign key (forum_id) references movies.forum(id) on delete cascade
);