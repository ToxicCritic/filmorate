INSERT INTO users (email, login, name, birthday) VALUES
('john.doe@example.com', 'johndoe', 'John Doe', '1985-05-15'),
('jane.smith@example.com', 'janesmith', 'Jane Smith', '1990-07-20');

INSERT INTO mpa_ratings (name, description) VALUES
('G', 'General Audiences'),
('PG', 'Parental Guidance Suggested'),
('PG-13', 'Parents Strongly Cautioned'),
('R', 'Restricted'),
('NC-17', 'Adults Only');

INSERT INTO genres (name) VALUES
('Action'),
('Comedy'),
('Drama'),
('Horror'),
('Sci-Fi');

INSERT INTO films (name, description, release_date, duration, mpa_rating_id) VALUES
('Inception', 'A mind-bending thriller', '2010-07-16', 148, 3),
('The Dark Knight', 'A gritty superhero tale', '2008-07-18', 152, 4);

INSERT INTO film_genres (film_id, genre_id) VALUES
(1, 1),
(1, 5),
(2, 1),
(2, 3);

INSERT INTO likes (film_id, user_id) VALUES
(1, 1),
(1, 2),
(2, 1);

INSERT INTO friendships (user_id, friend_id, status) VALUES
(1, 2, 'confirmed'),
(2, 1, 'confirmed');
