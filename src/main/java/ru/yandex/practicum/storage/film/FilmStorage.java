package ru.yandex.practicum.storage.film;

import ru.yandex.practicum.model.Film;

import java.util.Collection;

public interface FilmStorage {
    public Film addFilm(Film film);
    public Film updateFilm(Film film);
    public Collection<Film> getAllFilms();
    public Film deleteFilm(Film film);
}
