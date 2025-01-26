package ru.yandex.practicum.storage.like;

import ru.yandex.practicum.model.Film;

import java.util.List;

public interface LikeStorage {
    void addLike(Long filmId, Long userId);

    void removeLike(Long filmId, Long userId);

    List<Film> getPopularFilms(int count);
}