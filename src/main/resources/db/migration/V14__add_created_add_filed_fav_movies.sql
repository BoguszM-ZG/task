alter table movies.favourite_movies
    add created_at DATETIME default CURRENT_TIMESTAMP not null;

