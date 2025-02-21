package ru.yandex.practicum.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.storage.friend.FriendStorage;
import ru.yandex.practicum.storage.user.UserStorage;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserStorage userStorage;
    private final FriendStorage friendStorage;

    public UserService(UserStorage userStorage, FriendStorage friendStorage) {
        this.userStorage = userStorage;
        this.friendStorage = friendStorage;
    }

    public User createUser(User user) {
        return userStorage.save(user);
    }

    public User updateUser(User user) {
        return userStorage.update(user);
    }

    public List<User> getAllUsers() {
        return userStorage.findAll();
    }

    public Optional<User> getUserById(Integer id) {
        return userStorage.findById(id);
    }

    public void addFriend(Integer userId, Integer friendId) {
        friendStorage.addFriend(userId, friendId);
    }

    public void removeFriend(Integer userId, Integer friendId) {
        friendStorage.removeFriend(userId, friendId);
    }

    public List<User> getFriends(Integer userId) {
        return friendStorage.getFriends(userId);
    }

    public List<User> getCommonFriends(Integer userId, Integer otherId) {
        return friendStorage.getCommonFriends(userId, otherId);
    }
}