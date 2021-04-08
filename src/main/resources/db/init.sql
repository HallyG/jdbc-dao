CREATE TABLE IF NOT EXISTS users(
    user_id integer primary key,
    email text not null
);


INSERT INTO users (email) VALUES ('test@email.com');