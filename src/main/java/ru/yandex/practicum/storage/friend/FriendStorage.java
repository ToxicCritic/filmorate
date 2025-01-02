package ru.yandex.practicum.storage.friend;

import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public interface FriendStorage {
    public void addFriend(long userId, long friendId);

    public void removeFriend(long userId, long friendId);

    public Set<Long> getFriends(long userId);
}
