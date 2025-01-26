package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.service.UserService;
import ru.yandex.practicum.storage.friend.FriendDbStorage;
import ru.yandex.practicum.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@JdbcTest
@AutoConfigureTestDatabase(replace = NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({
        UserController.class,
        UserService.class,
        UserDbStorage.class,
        FriendDbStorage.class
})
@AutoConfigureWebMvc
class UserControllerTest {

    private final UserController userController;

    @Test
    @DisplayName("Создание нового пользователя: успех")
    void createUserSuccess() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("testlogin");
        user.setName("Test User");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        User created = userController.createUser(user);

        assertNotNull(created.getId(), "ID не должен быть null после создания");
        assertEquals("test@example.com", created.getEmail());
        assertEquals("testlogin", created.getLogin());
        assertEquals("Test User", created.getName());
        assertEquals(LocalDate.of(2000, 1, 1), created.getBirthday());
    }

    @Test
    @DisplayName("Обновление пользователя, который существует")
    void updateUserSuccess() {
        User user = new User();
        user.setEmail("init@example.com");
        user.setLogin("initlogin");
        user.setName("Initial User");
        user.setBirthday(LocalDate.of(1990, 5, 5));

        User created = userController.createUser(user);
        Integer newId = created.getId();

        created.setEmail("update@example.com");
        created.setName("Updated User");

        User updated = userController.updateUser(created);
        assertEquals(newId, updated.getId(), "ID не должен меняться при обновлении");
        assertEquals("update@example.com", updated.getEmail());
        assertEquals("Updated User", updated.getName());
    }

    @Test
    @DisplayName("Обновление пользователя, который не существует (должен бросить NotFoundException)")
    void updateUserNotFound() {
        User user = new User();
        user.setId(999);
        user.setEmail("noone@example.com");
        user.setLogin("none");
        user.setName("NoOne");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        assertThrows(
                NotFoundException.class,
                () -> userController.updateUser(user),
                "Ожидаем, что бросится NotFoundException при обновлении несуществующего пользователя"
        );
    }

    @Test
    @DisplayName("Получение всех пользователей")
    void getAllUsersEmpty() {
        List<User> all = userController.getAllUsers();
        assertNotNull(all, "Список пользователей не должен быть null");
        assertFalse(all.isEmpty(), "Список пользователей не должен быть пуст");
    }

    @Test
    @DisplayName("Получение пользователя по существующему ID")
    void getUserById() {
        User user = new User();
        user.setEmail("idtest@example.com");
        user.setLogin("idtest");
        user.setName("ID Test");
        user.setBirthday(LocalDate.of(1985, 3, 10));

        User created = userController.createUser(user);
        Integer newId = created.getId();

        User found = userController.getUserById(newId);
        assertEquals(created.getId(), found.getId());
        assertEquals("idtest@example.com", found.getEmail());
    }

    @Test
    @DisplayName("Получение пользователя по несуществующему ID бросает NotFoundException")
    void getUserByIdNotFound() {
        assertThrows(
                NotFoundException.class,
                () -> userController.getUserById(999),
                "Ожидаем NotFoundException при запросе несуществующего пользователя"
        );
    }

    @Test
    @DisplayName("Добавление друга пользователю")
    void addFriendSuccess() {
        User user1 = new User();
        user1.setEmail("a1@example.com");
        user1.setLogin("user1");
        user1.setName("User 1");
        user1.setBirthday(LocalDate.of(1990, 1, 1));
        User created1 = userController.createUser(user1);

        User user2 = new User();
        user2.setEmail("a2@example.com");
        user2.setLogin("user2");
        user2.setName("User 2");
        user2.setBirthday(LocalDate.of(1992, 2, 2));
        User created2 = userController.createUser(user2);

        userController.addFriend(created1.getId(), created2.getId());

        // в реальной реализации можно проверить через userController.getFriends(user1.id)
        List<User> friends = userController.getFriends(created1.getId());
        assertEquals(1, friends.size(), "Ожидаем одного друга у пользователя 1");
        assertEquals(created2.getId(), friends.get(0).getId(), "Другом должен быть пользователь 2");
    }

    @Test
    @DisplayName("Удаление друга")
    void removeFriend() {
        User user1 = new User();
        user1.setEmail("rm1@example.com");
        user1.setLogin("rm1");
        user1.setName("Remove1");
        user1.setBirthday(LocalDate.of(1990, 1, 1));
        User created1 = userController.createUser(user1);

        User user2 = new User();
        user2.setEmail("rm2@example.com");
        user2.setLogin("rm2");
        user2.setName("Remove2");
        user2.setBirthday(LocalDate.of(1992, 2, 2));
        User created2 = userController.createUser(user2);

        userController.addFriend(created1.getId(), created2.getId());
        assertEquals(1, userController.getFriends(created1.getId()).size());

        userController.removeFriend(created1.getId(), created2.getId());
        assertTrue(userController.getFriends(created1.getId()).isEmpty(), "Список друзей после удаления должен быть пуст");
    }

    @Test
    @DisplayName("Получение общих друзей")
    void getCommonFriends() {
        User user1 = new User();
        user1.setEmail("common1@example.com");
        user1.setLogin("cm1");
        user1.setName("Common1");
        user1.setBirthday(LocalDate.of(1990, 1, 1));
        User created1 = userController.createUser(user1);

        User user2 = new User();
        user2.setEmail("common2@example.com");
        user2.setLogin("cm2");
        user2.setName("Common2");
        user2.setBirthday(LocalDate.of(1992, 2, 2));
        User created2 = userController.createUser(user2);

        User user3 = new User();
        user3.setEmail("common3@example.com");
        user3.setLogin("cm3");
        user3.setName("Common3");
        user3.setBirthday(LocalDate.of(1993, 3, 3));
        User created3 = userController.createUser(user3);

        // user1 -> user3
        userController.addFriend(created1.getId(), created3.getId());
        // user2 -> user3
        userController.addFriend(created2.getId(), created3.getId());

        // Общее — user3
        List<User> common = userController.getCommonFriends(created1.getId(), created2.getId());
        assertEquals(1, common.size(), "Ожидаем одного общего друга");
        assertEquals(created3.getId(), common.get(0).getId(), "Общим другом должен быть user3");
    }
}