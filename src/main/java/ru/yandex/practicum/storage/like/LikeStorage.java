package ru.yandex.practicum.storage.like;

import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public interface LikeStorage {
    public void addLike(long filmId, long userId);

    public Set<Long> getLikes(long filmId);

    public void removeLike(long filmId, long userId);

    public int getLikeCount(long filmId);
}
