create table movies.report_message
(
    id bigint auto_increment primary key,
    message_id bigint not null,
    foreign key (message_id) references movies.messages(id) on delete cascade,
    reason varchar(500)
)