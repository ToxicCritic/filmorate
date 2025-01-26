package ru.yandex.practicum.storage.like;

import ru.yandex.practicum.model.Film;

import java.util.List;

public interface LikeStorage {
    void addLike(Integer filmId, Integer userId);

    void removeLike(Integer filmId, Integer userId);

    List<Film> getPopularFilms(int count);
}