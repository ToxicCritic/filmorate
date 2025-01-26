package ru.yandex.practicum.storage.mpa;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.MpaRating;
import ru.yandex.practicum.storage.mapper.MpaRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<MpaRating> findAll() {
        String sql = "SELECT * FROM mpa_ratings";
        return jdbcTemplate.query(sql, new MpaRowMapper());
    }

    @Override
    public Optional<MpaRating> findById(Long id) {
        String sql = "SELECT * FROM mpa_ratings WHERE id = ?";
        return jdbcTemplate.query(sql, new MpaRowMapper(), id).stream().findFirst();
    }
}