package ru.yandex.practicum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.storage.friend.FriendStorage;
import ru.yandex.practicum.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;
    private final FriendStorage friendStorage;

    @Autowired
    public UserService(UserStorage userStorage, FriendStorage friendStorage) {
        this.userStorage = userStorage;
        this.friendStorage = friendStorage;
    }

    public User save(User user) {
        return userStorage.save(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public User findById(Long id) {
        return userStorage.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public void deleteById(Long id) {
        userStorage.deleteById(id);
    }

    public void addFriend(long userId, long friendId) {
        friendStorage.addFriend(userId, friendId);
    }

    public void removeFriend(long userId, long friendId) {
        friendStorage.removeFriend(userId, friendId);
    }

    public Set<Long> getFriends(long userId) {
        return friendStorage.getFriends(userId);
    }

    public Collection<User> getCommonFriends(Long userId, Long otherId) {
        Set<Long> userFriends = friendStorage.getFriends(userId);
        Set<Long> otherFriends = friendStorage.getFriends(otherId);

        return userFriends.stream()
                .filter(otherFriends::contains)
                .map(id -> userStorage.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found")))
                .collect(Collectors.toList());
    }
}