package ru.yandex.practicum.storage.genre;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.Genre;
import ru.yandex.practicum.storage.mapper.GenreRowMapper;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class GenreDbStorage implements GenreStorage {
    private static final Logger logger = LoggerFactory.getLogger(GenreDbStorage.class);

    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> findAll() {
        String sql = "SELECT id, name FROM genres";
        logger.info("Выполнение запроса для получения всех жанров");
        return jdbcTemplate.query(sql, new GenreRowMapper());
    }

    @Override
    public Optional<Genre> findById(Integer id) {
        logger.info("Поиск жанра с ID: {}", id);
        String sql = "SELECT * FROM genres WHERE id = ?";
        try {
            Genre genre = jdbcTemplate.queryForObject(sql, new GenreRowMapper(), id);
            return Optional.ofNullable(genre);
        } catch (Exception e) {
            logger.warn("Жанр с ID {} не найден.", id);
            return Optional.empty();
        }
    }

    @Override
    public Set<Genre> findByFilmId(Integer filmId) {
        logger.info("Поиск жанров для фильма с ID: {}", filmId);
        String sql = "SELECT g.id, g.name FROM genres g " +
                "JOIN film_genres fg ON g.id = fg.genre_id " +
                "WHERE fg.film_id = ?";
        List<Genre> genres = jdbcTemplate.query(sql, new GenreRowMapper(), filmId);
        return new HashSet<>(genres);
    }
}