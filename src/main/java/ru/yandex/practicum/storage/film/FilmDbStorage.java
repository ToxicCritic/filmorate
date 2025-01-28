package ru.yandex.practicum.storage.film;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.Genre;
import ru.yandex.practicum.model.MpaRating;
import ru.yandex.practicum.storage.genre.GenreStorage;
import ru.yandex.practicum.storage.mapper.GenreRowMapper;
import ru.yandex.practicum.storage.mpa.MpaStorage;

import java.sql.PreparedStatement;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private static final Logger logger = LoggerFactory.getLogger(FilmDbStorage.class);

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
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

        saveGenres(film);

        MpaRating mpa = mpaStorage.findById(film.getMpaRating().getId())
                .orElseThrow(() -> {
                    String message = "MPA рейтинг с ID " + film.getMpaRating().getId() + " не найден.";
                    logger.error(message);
                    return new NotFoundException(message);
                });
        film.setMpaRating(mpa);
        logger.info("MPA Rating установлен: {}", mpa);

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

        MpaRating mpa = mpaStorage.findById(film.getMpaRating().getId())
                .orElseThrow(() -> {
                    String message = "MPA рейтинг с ID " + film.getMpaRating().getId() + " не найден.";
                    logger.error(message);
                    return new NotFoundException(message);
                });
        film.setMpaRating(mpa);
        logger.info("MPA Rating установлен: {}", mpa);

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
    @Transactional(readOnly = true)
    public List<Film> findAll() {
        logger.info("Получение списка всех фильмов с MPA рейтингами.");

        // 1. Получаем все фильмы с присоединёнными MPA рейтингами
        String sqlFilms = "SELECT f.id, f.name, f.description, f.release_date, f.duration, m.id as mpa_id, m.name as mpa_name, m.description as mpa_description " +
                "FROM films f " +
                "JOIN mpa_ratings m ON f.mpa_rating_id = m.id";
        List<Film> films = jdbcTemplate.query(sqlFilms, (rs, rowNum) -> {
            MpaRating mpa = new MpaRating(
                    rs.getInt("mpa_id"),
                    rs.getString("mpa_name"),
                    rs.getString("mpa_description")
            );

            return new Film(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getDate("release_date").toLocalDate(),
                    rs.getInt("duration"),
                    mpa,
                    new HashSet<>()
            );
        });

        if (films.isEmpty()) {
            logger.info("Фильмы не найдены.");
            return films;
        }

        // Собираем все filmIds
        Set<Integer> filmIds = films.stream()
                .map(Film::getId)
                .collect(Collectors.toSet());

        // 2. Получаем все жанры для этих фильмов за один запрос
        String sqlGenres = "SELECT fg.film_id, g.id, g.name " +
                "FROM film_genres fg " +
                "JOIN genres g ON fg.genre_id = g.id " +
                "WHERE fg.film_id IN (:filmIds)";

        // Тут не уверен, по ответам на StackOverflow singletonMap подходит
        Map<String, Object> params = Collections.singletonMap("filmIds", filmIds);

        List<Map<String, Object>> genreRows = namedParameterJdbcTemplate.queryForList(sqlGenres, params);
        Map<Integer, Set<Genre>> filmGenresMap = new HashMap<>();

        for (Map<String, Object> row : genreRows) {
            Integer filmId = (Integer) row.get("film_id");
            Genre genre = new Genre((Integer) row.get("id"), (String) row.get("name"));

            filmGenresMap.computeIfAbsent(filmId, k -> new HashSet<>()).add(genre);
        }

        films.forEach(film -> film.setGenres(filmGenresMap.getOrDefault(film.getId(), new HashSet<>())));

        logger.info("Найдено {} фильмов.", films.size());
        return films;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Film> findById(Integer id) {
        logger.info("Поиск фильма с ID: {}", id);
        String sql = "SELECT f.id, f.name, f.description, f.release_date, f.duration, " +
                "m.id as mpa_id, m.name as mpa_name, m.description as mpa_description " +
                "FROM films f " +
                "JOIN mpa_ratings m ON f.mpa_rating_id = m.id " +
                "WHERE f.id = ?";
        try {
            Film film = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                MpaRating mpa = new MpaRating(
                        rs.getInt("mpa_id"),
                        rs.getString("mpa_name"),
                        rs.getString("mpa_description")
                );

                return new Film(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDate("release_date").toLocalDate(),
                        rs.getInt("duration"),
                        mpa,
                        new HashSet<>()
                );
            }, id);

            if (film != null) {
                // Получаем жанры за один запрос
                String sqlGenres = "SELECT g.id, g.name " +
                        "FROM film_genres fg " +
                        "JOIN genres g ON fg.genre_id = g.id " +
                        "WHERE fg.film_id = ?";
                List<Genre> genres = jdbcTemplate.query(sqlGenres, new GenreRowMapper(), film.getId());
                film.setGenres(new HashSet<>(genres));

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