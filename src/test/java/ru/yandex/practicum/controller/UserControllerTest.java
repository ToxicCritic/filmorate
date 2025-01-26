package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.storage.user.UserDbStorage;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDbStorage.class})
public class UserControllerTest {

    @Autowired
    private UserDbStorage userStorage;

    @Test
    public void testFindUserById() {
        Optional<User> userOptional = userStorage.findById(1L);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
                                .hasFieldOrPropertyWithValue("email", "john.doe@example.com")
                                .hasFieldOrPropertyWithValue("login", "johndoe")
                                .hasFieldOrPropertyWithValue("name", "John Doe")
                                .hasFieldOrPropertyWithValue("birthday", java.time.LocalDate.of(1985, 5, 15))
                );
    }

    @Test
    public void testSaveUser() {
        User newUser = new User();
        newUser.setEmail("new.user@example.com");
        newUser.setLogin("newuser");
        newUser.setName("New User");
        newUser.setBirthday(java.time.LocalDate.of(1995, 3, 10));

        User savedUser = userStorage.save(newUser);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();

        Optional<User> retrievedUser = userStorage.findById(savedUser.getId());

        assertThat(retrievedUser)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("email", "new.user@example.com")
                                .hasFieldOrPropertyWithValue("login", "newuser")
                                .hasFieldOrPropertyWithValue("name", "New User")
                                .hasFieldOrPropertyWithValue("birthday", java.time.LocalDate.of(1995, 3, 10))
                );
    }

    @Test
    public void testUpdateUser() {
        User existingUser = userStorage.findById(1L).orElseThrow(() -> new RuntimeException("User not found"));
        existingUser.setName("Updated Name");

        userStorage.update(existingUser);

        Optional<User> updatedUser = userStorage.findById(1L);

        assertThat(updatedUser)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "Updated Name")
                );
    }

    @Test
    public void testDeleteUser() {
        userStorage.delete(1L);

        Optional<User> deletedUser = userStorage.findById(1L);

        assertThat(deletedUser).isNotPresent();
    }
}