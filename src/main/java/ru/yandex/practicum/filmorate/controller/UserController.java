package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping

    public List<User> getUsers() {
        log.info("Получение списка пользователей");
        return userService.getUsers();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Создан пользователь " + user.getId());
        return userService.createUser(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Обновлен пользователь " + user.getId());
        return userService.updateUser(user);

    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable long id) {
        log.info("Пользователь " + id);
        return userService.getUserById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addToFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info("Пользователь  " + friendId + " добавлен в друзья пользователю " + id);
        userService.addToFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFromFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info("Пользователь  " + friendId + " удален из друзей пользователя " + id);
        userService.deleteFromFriend(id, friendId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable long id, @PathVariable long otherId) {
        log.info("Запрос на получение общих друзей ");
        return userService.getCommonFriends(id, otherId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getAllFriends(@PathVariable long id) {
        log.info("Получение списка  друзей");
        return userService.getAllFriends(id);
    }

}