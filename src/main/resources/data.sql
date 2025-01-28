-- для использования конфигурации JDBC необходимо изменить свойство в файле
-- application.properties : spring.sql.init.mode=always

INSERT INTO users (email, login, name, birthday) VALUES
('john.doe@example.com', 'johndoe', 'John Doe', '1985-05-15'),
('jane.smith@example.com', 'janesmith', 'Jane Smith', '1990-07-20'),
('alice.wonderland@example.com', 'alice', 'Alice Wonderland', '1995-03-10'),
('bob.builder@example.com', 'builderbob', 'Bob Builder', '1988-11-23'),
('charlie.chaplin@example.com', 'charlie', 'Charlie Chaplin', '1975-04-16'),
('david.bowie@example.com', 'bowiedavid', 'David Bowie', '1947-01-08'),
('emily.dickinson@example.com', 'emilyd', 'Emily Dickinson', '1830-12-10'),
('frank.sinatra@example.com', 'sinatra', 'Frank Sinatra', '1915-12-12');

INSERT INTO mpa_ratings (name, description) VALUES
('G', 'General Audiences'),
('PG', 'Parental Guidance Suggested'),
('PG-13', 'Parents Strongly Cautioned'),
('R', 'Restricted'),
('NC-17', 'Adults Only');

INSERT INTO genres (name) VALUES
('Комедия'),
('Драма'),
('Боевик'),
('Триллер'),
('Фантастика'),
('Мультфильм'),
('Ужасы'),
('Романтика');

DELETE FROM films;

INSERT INTO films (name, description, release_date, duration, mpa_rating_id) VALUES
('Inception', 'A mind-bending thriller', '2010-07-16', 148, 2),
('The Matrix', 'A hacker discovers a shocking truth about reality', '1999-03-31', 136, 2),
('Interstellar', 'A journey through space and time', '2014-11-07', 169, 2),
('The Godfather', 'The rise of a mafia family', '1972-03-24', 175, 3),
('Pulp Fiction', 'Intersecting stories of crime', '1994-10-14', 154, 3),
('The Shawshank Redemption', 'A man''s resilience in prison', '1994-09-23', 142, 3),
('The Dark Knight', 'Batman faces the Joker', '2008-07-18', 152, 3),
('Forrest Gump', 'A journey through life', '1994-07-06', 142, 1);

INSERT INTO film_genres (film_id, genre_id) VALUES
(1, 4), (1, 5),
(2, 3), (2, 5),
(3, 5), (3, 2),
(4, 2), (4, 3),
(5, 3), (5, 4),
(6, 2), (6, 5),
(7, 3), (7, 4),
(8, 1), (8, 2);

INSERT INTO likes (film_id, user_id) VALUES
(1, 1), (1, 2), (1, 3),
(2, 4), (2, 5), (2, 6),
(3, 7), (3, 8), (4, 1),
(5, 2), (6, 3), (6, 4);

INSERT INTO friendships (user_id, friend_id, status) VALUES
(1, 2, 'CONFIRMED'),
(2, 3, 'CONFIRMED'),
(3, 4, 'CONFIRMED'),
(4, 5, 'CONFIRMED'),
(5, 6, 'CONFIRMED'),
(6, 7, 'CONFIRMED'),
(7, 8, 'CONFIRMED'),
(8, 1, 'CONFIRMED');