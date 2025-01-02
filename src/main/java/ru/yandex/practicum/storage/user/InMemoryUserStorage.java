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
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long nextId = 1;

    @Override
    public User addUser(User user) {
        user.setId(nextId++);
        users.put(user.getId(), user);
        log.info("Пользователь добавлен: {}", user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Пользователь с таким ID не найден.");
        }
        users.put(user.getId(), user);
        log.info("Пользователь обновлен: {}", user);
        return user;
    }

    @Override
    public User deleteUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Пользователь с таким ID не найден.");
        }
        users.remove(user.getId());
        log.info("Пользователь был удален: {}", user);
        return user;
    }

    @Override
    public Collection<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(Long id) {
        User user = users.get(id);
        if (user == null) {
            throw new ValidationException("Пользователь с таким ID не найден.");
        }
        return user;
    }
}
