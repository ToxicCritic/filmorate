package ru.yandex.practicum.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.storage.film.FilmStorage;
import ru.yandex.practicum.storage.like.LikeStorage;

import java.util.List;
import java.util.Optional;

@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final LikeStorage likeStorage;

    public FilmService(FilmStorage filmStorage, LikeStorage likeStorage) {
        this.filmStorage = filmStorage;
        this.likeStorage = likeStorage;
    }

    public Film createFilm(Film film) {
        return filmStorage.save(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.update(film);
    }

    public List<Film> getAllFilms() {
        return filmStorage.findAll();
    }

    public Optional<Film> getFilmById(Long id) {
        return filmStorage.findById(id);
    }

    public void addLike(Long filmId, Long userId) {
        likeStorage.addLike(filmId, userId);
    }

    public void removeLike(Long filmId, Long userId) {
        likeStorage.removeLike(filmId, userId);
    }

    public List<Film> getPopularFilms(int count) {
        return likeStorage.getPopularFilms(count);
    }
}
