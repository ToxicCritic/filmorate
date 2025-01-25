package ru.yandex.practicum.storage.friend;

import java.util.Set;

public interface FriendStorage {
    void addFriend(long userId, long friendId);
    void removeFriend(long userId, long friendId);
    Set<Long> getFriends(long userId);
}