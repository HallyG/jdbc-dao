CREATE TABLE IF NOT EXISTS users(
    user_id integer primary key,
    email text not null unique
);


INSERT INTO users (email) VALUES ('test@email.com');