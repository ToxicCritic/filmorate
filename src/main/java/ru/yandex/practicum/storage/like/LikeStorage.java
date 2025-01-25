package ru.yandex.practicum.storage.like;

public interface LikeStorage {
    void addLike(long filmId, long userId);

    void removeLike(long filmId, long userId);

    int getLikeCount(long filmId);
}