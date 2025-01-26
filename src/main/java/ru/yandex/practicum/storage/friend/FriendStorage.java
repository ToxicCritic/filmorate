package ru.yandex.practicum.storage.friend;

import ru.yandex.practicum.model.User;

import java.util.List;

public interface FriendStorage {
    void addFriend(Long userId, Long friendId);

    void removeFriend(Long userId, Long friendId);

    List<User> getFriends(Long userId);

    List<User> getCommonFriends(Long userId, Long otherId);
}