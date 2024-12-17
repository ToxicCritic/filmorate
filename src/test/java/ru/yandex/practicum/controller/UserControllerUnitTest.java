package ru.yandex.practicum.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.exception.ValidationException;
import ru.yandex.practicum.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerUnitTest {

    private UserController userController;

    @BeforeEach
    public void setup() {
        userController = new UserController();
    }

    @Test
    public void shouldCreateUserSuccessfully() {
        User user = new User();
        user.setLogin("testuser");
        user.setEmail("test@mail.com");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        User createdUser = userController.createUser(user);

        assertNotNull(createdUser);
        assertEquals(1, createdUser.getId());
        assertEquals("testuser", createdUser.getLogin());
    }

    @Test
    public void shouldSetLoginAsNameIfNameIsEmpty() {
        User user = new User();
        user.setLogin("autoname");
        user.setEmail("auto@mail.com");
        user.setBirthday(LocalDate.of(1995, 5, 15));

        User createdUser = userController.createUser(user);

        assertEquals("autoname", createdUser.getName());
    }

    @Test
    public void shouldThrowExceptionForUpdatingNonExistentUser() {
        User user = new User();
        user.setId(999L);
        user.setLogin("nonexistent");

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            userController.updateUser(user);
        });

        assertEquals("Пользователь с таким ID не найден.", exception.getMessage());
    }
}