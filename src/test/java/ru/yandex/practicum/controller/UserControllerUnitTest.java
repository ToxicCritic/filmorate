package ru.yandex.practicum.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.exception.ValidationException;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.service.UserService;
import ru.yandex.practicum.storage.friend.FriendDbStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerUnitTest {

    private UserController userController;

    @BeforeEach
    public void setup() {
        UserService userService = new UserService(new InMemoryUserStorage(), new FriendDbStorage());
        userController = new UserController(userService);
    }

    @Test
    public void shouldAddUserSuccessfully() {
        User user = new User();
        user.setLogin("testuser");
        user.setEmail("test@mail.com");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        User createdUser = userController.addUser(user);

        assertNotNull(createdUser);
        assertEquals(1, createdUser.getId());
        assertEquals("testuser", createdUser.getLogin());
    }

    @Test
    public void shouldUpdateUserSuccessfully() {
        User user = new User();
        user.setLogin("testuser");
        user.setEmail("test@mail.com");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        User createdUser = userController.addUser(user);

        createdUser.setLogin("updatedUser");
        User updatedUser = userController.updateUser(createdUser);

        assertNotNull(updatedUser);
        assertEquals(createdUser.getId(), updatedUser.getId());
        assertEquals("updatedUser", updatedUser.getLogin());
    }

    @Test
    public void shouldDeleteUserSuccessfully() {
        User user = new User();
        user.setLogin("testuser");
        user.setEmail("test@mail.com");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        User createdUser = userController.addUser(user);

        assertDoesNotThrow(() -> userController.deleteUser(createdUser));

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            userController.getUserById(createdUser.getId());
        });

        assertEquals("Пользователь с таким ID не найден.", exception.getMessage());
    }

    @Test
    public void shouldGetUserByIdSuccessfully() {
        User user = new User();
        user.setLogin("testuser");
        user.setEmail("test@mail.com");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        User createdUser = userController.addUser(user);

        User retrievedUser = userController.getUserById(createdUser.getId());

        assertNotNull(retrievedUser);
        assertEquals(createdUser.getId(), retrievedUser.getId());
        assertEquals("testuser", retrievedUser.getLogin());
    }

}