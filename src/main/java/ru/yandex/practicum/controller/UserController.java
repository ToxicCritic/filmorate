package ru.yandex.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@Validated
public class UserController {

    private final List<User> users = new ArrayList<>();

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(users.size() + 1);
        users.add(user);
        log.info("Пользователь создан: {}", user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        users.set(user.getId() - 1, user);
        log.info("Пользователь обновлен: {}", user);
        return user;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return users;
    }
}
