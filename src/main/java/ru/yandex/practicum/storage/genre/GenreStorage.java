package ru.yandex.practicum.storage.genre;

import ru.yandex.practicum.model.Genre;
import java.util.List;
import java.util.Optional;

public interface GenreStorage {
    List<Genre> findAll();

    Optional<Genre> findById(int id);
}