package ru.yandex.practicum.storage.friend;

import ru.yandex.practicum.model.User;

import java.util.List;

public interface FriendStorage {
    void addFriend(Integer userId, Integer friendId);

    void removeFriend(Integer userId, Integer friendId);

    List<User> getFriends(Integer userId);

    List<User> getCommonFriends(Integer userId, Integer otherId);
}