package ru.yandex.practicum.storage.film;

import ru.yandex.practicum.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    Film save(Film film);

    Film update(Film film);

    List<Film> findAll();

    Optional<Film> findById(Long id);

    void delete(Long id);
}