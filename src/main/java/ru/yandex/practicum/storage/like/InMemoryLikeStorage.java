package ru.yandex.practicum.storage.like;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class InMemoryLikeStorage implements LikeStorage {

    private final Map<Long, Set<Long>> likes = new HashMap<>(); // filmId -> userIds

    public void addLike(long filmId, long userId) {
        likes.computeIfAbsent(filmId, k -> new HashSet<>()).add(userId);
    }

    public void removeLike(long filmId, long userId) {
        if (likes.containsKey(filmId)) {
            likes.get(filmId).remove(userId);
            if (likes.get(filmId).isEmpty()) {
                likes.remove(filmId);
            }
        }
    }

    public Set<Long> getLikes(long filmId) {
        return likes.getOrDefault(filmId, new HashSet<>());
    }

    public int getLikeCount(long filmId) {
        return getLikes(filmId).size();
    }
}
