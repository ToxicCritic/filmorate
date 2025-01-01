package ru.yandex.practicum.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.exception.ValidationException;
import ru.yandex.practicum.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private long nextId = 1;

    @Override
    public Film addFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года.");
        }
        film.setId(nextId++);
        films.put(film.getId(), film);
        log.info("Фильм добавлен: {}", film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Фильм с таким ID не найден.");
        }
        films.put(film.getId(), film);
        log.info("Фильм обновлен: {}", film);
        return film;
    }

    @Override
    public Collection<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film deleteFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Фильм с таким ID не найден.");
        }
        films.remove(film.getId());
        log.info("Фильм был удален: {}", film);
        return film;
    }

    @Override
    public Film getFilmById(Long id) {
        Film film = films.get(id);
        if (film == null) {
            throw new ValidationException("Фильм с таким ID не найден.");
        }
        return film;
    }
}
