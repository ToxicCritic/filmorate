package ru.yandex.practicum.storage.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.model.MpaRating;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MpaRowMapper implements RowMapper<MpaRating> {
    @Override
    public MpaRating mapRow(ResultSet rs, int rowNum) throws SQLException {
        MpaRating mpaRating = new MpaRating();
        mpaRating.setId(rs.getInt("id"));
        mpaRating.setName(rs.getString("name"));
        mpaRating.setDescription(rs.getString("description"));
        return mpaRating;
    }
}