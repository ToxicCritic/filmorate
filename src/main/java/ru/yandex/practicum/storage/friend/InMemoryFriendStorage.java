package ru.yandex.practicum.storage.friend;


import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class InMemoryFriendStorage implements FriendStorage {

    private final Map<Long, Set<Long>> friends = new HashMap<>(); // userId -> friendIds

    public void addFriend(long userId, long friendId) {
        friends.computeIfAbsent(userId, k -> new HashSet<>()).add(friendId);
        friends.computeIfAbsent(friendId, k -> new HashSet<>()).add(userId);
    }

    public void removeFriend(long userId, long friendId) {
        if (friends.containsKey(userId)) {
            friends.get(userId).remove(friendId);
            if (friends.get(userId).isEmpty()) {
                friends.remove(userId);
            }
        }
        if (friends.containsKey(friendId)) {
            friends.get(friendId).remove(userId);
            if (friends.get(friendId).isEmpty()) {
                friends.remove(friendId);
            }
        }
    }

    public Set<Long> getFriends(long userId) {
        return friends.getOrDefault(userId, new HashSet<>());
    }
}
