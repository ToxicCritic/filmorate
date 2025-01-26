package ru.yandex.practicum.storage.mpa;

import ru.yandex.practicum.model.MpaRating;

import java.util.List;
import java.util.Optional;

public interface MpaStorage {
    List<MpaRating> findAll();

    Optional<MpaRating> findById(Long id);
}