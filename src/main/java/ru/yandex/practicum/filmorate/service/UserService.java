package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service

public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {

        this.userStorage = userStorage;
    }

    public User createUser(User user) {
        validateUser(user);
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        validateUser(user);
        return userStorage.updateUser(user);
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public User getUserById(long id) {
        return userStorage.getUserById(id);
    }

    public void addToFriend(long userId, long friendId) {
        if (getUserById(userId).getFriends().contains(friendId)) {
            log.warn("Пользователи уже друзья");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Пользователь " + userId + " уже жружит с " + friendId);

        }
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);

        log.info("Пользователь {} добавлен в друзья к {}", friendId, userId);
        log.info("Пользователь {} добавлен в друзья к {}", userId, friendId);
    }

    public void deleteFromFriend(long userId, long friendId) {
        if (!userStorage.getUserById(userId).getFriends().contains(friendId)) {
            log.warn("Пользователи не друзья",
                    userId, friendId);
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Пользователя " + userId + " нет в друзьях у пользователя " + friendId);
        }
        getUserById(userId).getFriends().remove(friendId);
        getUserById(friendId).getFriends().remove(userId);
        log.info("Пользователь {} удалён из друзей {}", friendId, userId);
        log.info("Пользователь {} удалён из друзей {}", userId, friendId);

    }


    public List<User> getCommonFriends(long userId, long friendId) {
        return getUserById(userId).getFriends()
                .stream()
                .filter(getUserById(friendId).getFriends()::contains)
                .map(this::getUserById)
                .collect(Collectors.toList());
    }

    public List<User> getAllFriends(long id) {
        return getUserById(id).getFriends()
                .stream()
                .map(this::getUserById)
                .collect(Collectors.toList());
    }

    public void validateUser(User user) {
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
}