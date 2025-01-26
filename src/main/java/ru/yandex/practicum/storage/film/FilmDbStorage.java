package ru.yandex.practicum.storage.film;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.Genre;
import ru.yandex.practicum.model.MpaRating;
import ru.yandex.practicum.storage.genre.GenreStorage;
import ru.yandex.practicum.storage.mapper.FilmRowMapper;
import ru.yandex.practicum.storage.mpa.MpaStorage;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private static final Logger logger = LoggerFactory.getLogger(FilmDbStorage.class);

    private final JdbcTemplate jdbcTemplate;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;

    @Override
    @Transactional
    public Film save(Film film) {
        logger.info("Сохранение нового фильма: {}", film.getName());
        String sql = "INSERT INTO films (name, description, release_date, duration, mpa_rating_id) VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, java.sql.Date.valueOf(film.getReleaseDate()));
            ps.setInt(4, film.getDuration());
            ps.setInt(5, film.getMpaRating().getId());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key != null) {
            film.setId(key.intValue());
            logger.info("Фильм сохранен с ID: {}", film.getId());
        } else {
            logger.error("Не удалось получить сгенерированный ID для фильма: {}", film.getName());
            throw new RuntimeException("Не удалось получить сгенерированный ID для фильма.");
        }

        // Сохранение жанров
        saveGenres(film);

        // Установка полного объекта MPA рейтинга
        MpaRating mpa = mpaStorage.findById(film.getMpaRating().getId())
                .orElseThrow(() -> {
                    String message = "MPA рейтинг с ID " + film.getMpaRating().getId() + " не найден.";
                    logger.error(message);
                    return new NotFoundException(message);
                });
        film.setMpaRating(mpa);
        logger.info("MPA Rating установлен: {}", mpa);

        // Установка жанров
        Set<Genre> genres = genreStorage.findByFilmId(film.getId());
        film.setGenres(genres);
        logger.info("Жанры установлены: {}", genres);

        logger.info("Сохранённый фильм: {}", film);
        return film;
    }

    @Override
    @Transactional
    public Film update(Film film) {
        logger.info("Обновление фильма с ID: {}", film.getId());
        String sql = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, mpa_rating_id = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                java.sql.Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpaRating().getId(),
                film.getId());

        if (rowsAffected == 0) {
            logger.warn("Фильм с ID {} не найден для обновления.", film.getId());
            throw new NotFoundException("Фильм с ID " + film.getId() + " не найден.");
        }

        deleteGenres(film.getId());
        saveGenres(film);

        // Установка полного объекта MPA рейтинга
        MpaRating mpa = mpaStorage.findById(film.getMpaRating().getId())
                .orElseThrow(() -> {
                    String message = "MPA рейтинг с ID " + film.getMpaRating().getId() + " не найден.";
                    logger.error(message);
                    return new NotFoundException(message);
                });
        film.setMpaRating(mpa);
        logger.info("MPA Rating установлен: {}", mpa);

        // Установка жанров
        Set<Genre> genres = genreStorage.findByFilmId(film.getId());
        film.setGenres(genres);
        logger.info("Жанры установлены: {}", genres);

        logger.info("Фильм с ID {} успешно обновлён: {}", film.getId(), film);
        return film;
    }

    @Override
    public void delete(Integer id) {
        logger.info("Удаление фильма с ID: {}", id);
        String sql = "DELETE FROM films WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        if (rowsAffected == 0) {
            logger.warn("Фильм с ID {} не найден для удаления.", id);
            throw new NotFoundException("Фильм с ID " + id + " не найден.");
        }
        logger.info("Фильм с ID {} успешно удалён.", id);
    }

    @Override
    public List<Film> findAll() {
        logger.info("Получение списка всех фильмов.");
        String sql = "SELECT * FROM films";
        List<Film> films = jdbcTemplate.query(sql, new FilmRowMapper());

        // Установка MPA рейтингов и жанров для каждого фильма
        films.forEach(film -> {
            MpaRating mpa = mpaStorage.findById(film.getMpaRating().getId())
                    .orElseThrow(() -> {
                        String message = "MPA рейтинг с ID " + film.getMpaRating().getId() + " не найден.";
                        logger.error(message);
                        return new NotFoundException(message);
                    });
            film.setMpaRating(mpa);
            Set<Genre> genres = genreStorage.findByFilmId(film.getId());
            film.setGenres(genres);
        });

        logger.info("Найдено {} фильмов.", films.size());
        return films;
    }

    @Override
    public Optional<Film> findById(Integer id) {
        logger.info("Поиск фильма с ID: {}", id);
        String sql = "SELECT * FROM films WHERE id = ?";
        try {
            Film film = jdbcTemplate.queryForObject(sql, new FilmRowMapper(), id);
            if (film != null) {
                MpaRating mpa = mpaStorage.findById(film.getMpaRating().getId())
                        .orElseThrow(() -> {
                            String message = "MPA рейтинг с ID " + film.getMpaRating().getId() + " не найден.";
                            logger.error(message);
                            return new NotFoundException(message);
                        });
                film.setMpaRating(mpa);
                Set<Genre> genres = genreStorage.findByFilmId(film.getId());
                film.setGenres(genres);
                logger.info("Фильм с ID {} найден: {}", id, film);
                return Optional.of(film);
            } else {
                logger.warn("Фильм с ID {} не найден.", id);
                return Optional.empty();
            }
        } catch (Exception e) {
            logger.warn("Фильм с ID {} не найден.", id);
            return Optional.empty();
        }
    }

    private void saveGenres(Film film) {
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            logger.info("Сохранение {} жанров для фильма с ID: {}", film.getGenres().size(), film.getId());
            String sql = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
            List<Object[]> batchArgs = film.getGenres().stream()
                    .map(genre -> new Object[]{film.getId(), genre.getId()})
                    .collect(Collectors.toList());
            jdbcTemplate.batchUpdate(sql, batchArgs);
            logger.info("Жанры для фильма с ID {} успешно сохранены.", film.getId());
        }
    }

    private void deleteGenres(Integer filmId) {
        logger.info("Удаление жанров для фильма с ID: {}", filmId);
        String sql = "DELETE FROM film_genres WHERE film_id = ?";
        jdbcTemplate.update(sql, filmId);
        logger.info("Жанры для фильма с ID {} успешно удалены.", filmId);
    }
}