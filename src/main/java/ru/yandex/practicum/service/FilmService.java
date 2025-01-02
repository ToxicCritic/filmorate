package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.exception.ValidationException;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.storage.film.FilmStorage;
import ru.yandex.practicum.storage.like.LikeStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final LikeStorage likeStorage;

    public Film addFilm(Film film) {
        validateFilm(film);
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        validateFilm(film);
        return filmStorage.updateFilm(film);
    }

    public Film deleteFilm(Film film) {
        return filmStorage.deleteFilm(film);
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(Long id) {
        return filmStorage.getFilmById(id);
    }

    public void addLike(long filmId, long userId) {
        Film film = getFilmById(filmId);
        likeStorage.addLike(filmId, userId);
    }

    public void removeLike(long filmId, long userId) {
        Film film = getFilmById(filmId);
        likeStorage.removeLike(filmId, userId);
    }

    public int getLikeCount(long filmId) {
        return likeStorage.getLikeCount(filmId);
    }

    public Collection<Film> getPopularFilms(int count) {
        return filmStorage.getAllFilms().stream()
                .sorted((f1, f2) -> Integer.compare(
                        likeStorage.getLikeCount(f2.getId()),
                        likeStorage.getLikeCount(f1.getId())))
                .limit(count)
                .collect(Collectors.toList());
    }

    private void validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года.");
        }
    }
}