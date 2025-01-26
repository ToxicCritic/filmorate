package ru.yandex.practicum.storage.user;

import ru.yandex.practicum.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    User save(User user);

    User update(User user);

    List<User> findAll();

    Optional<User> findById(Long id);

    void delete(Long id);

    void addFriend(Long userId, Long friendId);

    void removeFriend(Long userId, Long friendId);

    List<User> getFriends(Long userId);

    List<User> getCommonFriends(Long userId, Long otherId);
}