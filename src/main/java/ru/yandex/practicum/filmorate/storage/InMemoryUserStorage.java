package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/*

@Slf4j
@Component

public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> userMap = new HashMap<>();
    private static long userId = 1;

    @Override
    public User createUser(User user) {
        if (userMap.containsKey(user.getId()) || userMap.containsKey(user.getEmail())) {
            log.warn("Пользователь уже существует");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Пользователь уже существует");
        } else {
            //validateUser(user);
            user.setId(userId);
            userMap.put(user.getId(), user);
            userId++;
            log.info("Пользователь с id" + user.getId() + "спешно создан");
            return user;
        }
    }

    @Override
    public User updateUser(User user) {
        long id = user.getId();
        if (!userMap.containsKey(id)) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "Пользователя не существует");
        } else {
           // validateUser(user);
            userMap.replace(id, user);
            log.info("Пользователь обнавлен");
            return user;
        }
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(userMap.values());
    }

    @Override
    public User getUserById(long id) {
        if (!userMap.containsKey(id)) {
            log.error("Пользователя не существует");
            throw new ValidationException(HttpStatus.NOT_FOUND, "Пользователя не существует");
        }
        return userMap.get(id);
    }


   /* public void validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty() || !user.getEmail().contains("@") || user.getEmail().isBlank()) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Email должен быть заполнен и содержать символ @");
        } else if (user.getLogin() == null || user.getLogin().isEmpty() || user.getLogin().contains(" ") || user.getLogin().isBlank()) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Логин не должен быть пустым и содержать пробелы");
        } else if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Дата рождения не может быть в будущем");
        }
        if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

    }

    */



