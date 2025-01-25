package ru.yandex.practicum.storage.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.model.MpaRating;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MpaRowMapper implements RowMapper<MpaRating> {
    @Override
    public MpaRating mapRow(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("id");
        return MpaRating.values()[id - 1];
    }
}
