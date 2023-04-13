package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.*;

@RestController
@RequestMapping("/users")
@Slf4j


public class UserController {
    private final Map<Integer, User> userMap = new HashMap<>();
    private static int userId = 1;
    private boolean isUserValid = false;

    @GetMapping
            /*public Collection<User> getAllUsers() {
        log.info("Список пользователей");
        return new ArrayList<>(userMap.values());
    }

             */
    public List<User> getAllUsers() {
        return new ArrayList<>(userMap.values());
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        if (userMap.containsKey(user.getId()) || userMap.containsKey(user.getEmail())) {
            log.warn("Пользователь уже существует");
            throw new ValidationException("Пользователь уже существует");
        } else {
            validateName(user);
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
            validateName(user);
            userMap.replace(id, user);
            log.info("Пользователь обнавлен");
            return user;
        }
    }
private void validateName(User user){
        if(user.getName() == null || user.getName().isBlank()){
            user.setName(user.getLogin());

        }
}

}
