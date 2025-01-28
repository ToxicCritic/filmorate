package ru.yandex.practicum.storage.like;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.storage.genre.GenreStorage;
import ru.yandex.practicum.storage.mapper.FilmRowMapper;
import ru.yandex.practicum.storage.mpa.MpaStorage;

import java.util.List;

@Repository
public class LikeDbStorage implements LikeStorage {
    private static final Logger logger = LoggerFactory.getLogger(LikeDbStorage.class);

    private final JdbcTemplate jdbcTemplate;
    private final GenreStorage genreStorage;
    private final MpaStorage mpaStorage;

    public LikeDbStorage(JdbcTemplate jdbcTemplate, GenreStorage genreStorage, MpaStorage mpaStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreStorage = genreStorage;
        this.mpaStorage = mpaStorage;
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
        logger.info("Добавление лайка: filmId={}, userId={}", filmId, userId);
        String sql = "INSERT INTO likes (film_id, user_id) VALUES (?, ?)";
        try {
            jdbcTemplate.update(sql, filmId, userId);
            logger.info("Лайк добавлен: filmId={}, userId={}", filmId, userId);
        } catch (DataIntegrityViolationException e) {
            logger.warn("Не удалось добавить лайк: filmId={}, userId={}. Возможно, лайк уже существует.", filmId, userId);
            throw new IllegalArgumentException("Лайк уже существует.");
        }
    }

    @Override
    public void removeLike(Integer filmId, Integer userId) {
        logger.info("Удаление лайка: filmId={}, userId={}", filmId, userId);
        String sql = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, filmId, userId);
        if (rowsAffected == 0) {
            logger.warn("Лайк не найден для удаления: filmId={}, userId={}", filmId, userId);
            throw new IllegalArgumentException("Лайк не найден.");
        }
        logger.info("Лайк удален: filmId={}, userId={}", filmId, userId);
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        logger.info("Получение {} популярных фильмов.", count);
        String sql = "SELECT f.*, COUNT(l.user_id) AS like_count FROM films f " +
                "LEFT JOIN likes l ON f.id = l.film_id " +
                "GROUP BY f.id ORDER BY like_count DESC LIMIT ?";
        List<Film> films = jdbcTemplate.query(sql, new FilmRowMapper(), count);
        films.forEach(film -> {
            film.setMpaRating(mpaStorage.findById(film.getMpaRating().getId())
                    .orElseThrow(() -> new IllegalArgumentException("MPA рейтинг с ID " + film.getMpaRating().getId() + " не найден.")));
            film.setGenres(genreStorage.findByFilmId(film.getId()));
        });
        logger.info("Получено {} популярных фильмов.", films.size());
        return films;
    }
}