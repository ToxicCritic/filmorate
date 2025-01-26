package ru.yandex.practicum.controller;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.service.UserService;


import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@Validated
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@Valid @RequestBody User user) {
        if (userService.getUserById(user.getId()).isEmpty()) {
            throw new NotFoundException("Other user not found");
        }
        return userService.updateUser(user);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User getUserById(@PathVariable Integer id) {
        return userService.getUserById(id)
                .orElseThrow(() -> new NotFoundException("User with ID " + id + " not found"));
    }

    @PutMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        if (userService.getUserById(friendId).isEmpty()) {
            throw new NotFoundException("Friend not found");
        }
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        if (userService.getUserById(id).isEmpty()) {
            throw  new NotFoundException("User with ID " + id + " not found");
        } else if (userService.getUserById(friendId).isEmpty()) {
            throw new NotFoundException("Friend with ID " + friendId + " not found");
        }
        userService.removeFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getFriends(@PathVariable Integer id) {
        if (userService.getUserById(id).isEmpty()) {
            throw new NotFoundException("User not found");
        }
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getCommonFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        if (userService.getUserById(otherId).isEmpty()) {
            throw new NotFoundException("Other user not found");
        }
        return userService.getCommonFriends(id, otherId);
    }
}