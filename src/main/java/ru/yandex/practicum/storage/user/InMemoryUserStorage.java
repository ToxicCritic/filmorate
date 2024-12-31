package ru.yandex.practicum.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.exception.ValidationException;
import ru.yandex.practicum.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage {
    private final Map<Long, User> users = new HashMap<>();

    public User addUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        long newId = users.size() + 1;
        user.setId(newId);
        users.put(newId, user);
        log.info("Пользователь создан: {}", user);
        return user;
    }

    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Пользователь с таким ID не найден.");
        }
        users.put(user.getId(), user);
        log.info("Пользователь обновлен: {}", user);
        return user;
    }

    public User deleteUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Пользователь с таким ID не найден.");
        }
        users.remove(user.getId());
        log.info("Пользователь был удален: {}", user);
        return user;
    }

    public Collection<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
}
