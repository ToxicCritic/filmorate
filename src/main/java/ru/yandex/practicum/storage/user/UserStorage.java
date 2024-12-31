package ru.yandex.practicum.storage.user;

import ru.yandex.practicum.model.User;

import java.util.Collection;

public interface UserStorage {
    public User addUser(User user);
    public User updateUser(User user);
    public User deleteUser(User user);
    public Collection<User> getAllUsers();
}
