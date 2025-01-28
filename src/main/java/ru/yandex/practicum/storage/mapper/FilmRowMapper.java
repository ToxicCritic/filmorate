package ru.yandex.practicum.storage.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.MpaRating;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FilmRowMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getInt("id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());
        film.setDuration(rs.getInt("duration"));
        MpaRating mpaRating = new MpaRating();
        mpaRating.setId(rs.getInt("mpa_rating_id"));
        film.setMpaRating(mpaRating);
        return film;
    }
}