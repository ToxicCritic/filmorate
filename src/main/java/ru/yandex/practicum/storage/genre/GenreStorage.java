package ru.yandex.practicum.storage.genre;

import ru.yandex.practicum.model.Genre;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GenreStorage {
    List<Genre> findAll();

    Optional<Genre> findById(Integer id);

    Set<Genre> findByFilmId(Integer filmId);
}