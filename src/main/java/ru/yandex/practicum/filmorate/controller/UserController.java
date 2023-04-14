package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j


public class UserController {
    private final Map<Integer, User> userMap = new HashMap<>();
    private static int userId = 1;
    private boolean isUserValid = false;

    @GetMapping

    public List<User> getUsers() {
        return new ArrayList<>(userMap.values());
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        if (userMap.containsKey(user.getId()) || userMap.containsKey(user.getEmail())) {
            log.warn("Пользователь уже существует");
            throw new ValidationException("Пользователь уже существует");
        } else {
            validateUser(user);
            user.setId(userId);
            userMap.put(user.getId(), user);
            userId++;
            log.info("Пользователь с id" + user.getId() + "спешно создан");
            return user;
        }
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        int id = user.getId();
        if (!userMap.containsKey(id)) {
            throw new ValidationException("Пользователя не существует");
        } else {
            validateUser(user);
            userMap.replace(id, user);
            log.info("Пользователь обнавлен");
            return user;
        }

    }

    private void validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty() ||
                !user.getEmail().contains("@") || user.getEmail().isBlank()) {
            throw new ValidationException("Email должен быть заполнен и содержать символ @");
        } else if (user.getLogin() == null || user.getLogin().isEmpty() || user.getLogin().contains(" ") || user.getLogin().isBlank()) {
            throw new ValidationException("Логин не должен быть пустым и содержать пробелы");
        } else if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }


}
