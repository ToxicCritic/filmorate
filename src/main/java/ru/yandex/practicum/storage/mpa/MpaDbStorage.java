package ru.yandex.practicum.storage.mpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.MpaRating;
import ru.yandex.practicum.storage.mapper.MpaRowMapper;

import java.util.List;
import java.util.Optional;

@Repository
public class MpaDbStorage implements MpaStorage {

    private static final Logger logger = LoggerFactory.getLogger(MpaDbStorage.class);

    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<MpaRating> findAll() {
        String sql = "SELECT id, name, description FROM mpa_ratings";
        logger.info("Выполнение запроса для получения всех MPA рейтингов");
        return jdbcTemplate.query(sql, new MpaRowMapper());
    }

    @Override
    public Optional<MpaRating> findById(Integer id) {
        logger.info("Поиск MPA рейтинга с ID: {}", id);
        String sql = "SELECT * FROM mpa_ratings WHERE id = ?";
        try {
            MpaRating mpaRating = jdbcTemplate.queryForObject(sql, new MpaRowMapper(), id);
            return Optional.ofNullable(mpaRating);
        } catch (Exception e) {
            logger.warn("MPA рейтинг с ID {} не найден.", id);
            return Optional.empty();
        }
    }
}