package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.storage.friend.FriendStorage;
import ru.yandex.practicum.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserStorage userStorage;
    private final FriendStorage friendStorage;

    public User addUser(User user) {
        validateUser(user);
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        validateUser(user);
        return userStorage.updateUser(user);
    }

    public User deleteUser(User user) {
        return userStorage.deleteUser(user);
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUserById(Long id) {
        return userStorage.getUserById(id);
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
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }

    private void validateUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}