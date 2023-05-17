package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

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
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
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
            throw new ValidationException(HttpStatus.CONFLICT, "Пользователь " + userId + " уже жружит с " + friendId);

        }
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);

        log.info("Пользователь " + friendId + " добавлен в друзья к " + userId);
        log.info("Пользователь " + userId + " добавлен в друзья к " + friendId);
    }

    public void deleteFromFriend(long userId, long friendId) {
        if (!userStorage.getUserById(userId).getFriends().contains(friendId)) {
            log.warn("Пользователи не друзья",
                    userId, friendId);
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Пользователя " + userId + " нет в друзьях у пользователя " + friendId);
        }
        getUserById(userId).getFriends().remove(friendId);
        getUserById(friendId).getFriends().remove(userId);
        log.info("Пользователь " + friendId + "удалён из друзей " + userId);
        log.info("Пользователь " + userId + " удалён из друзей " + friendId);

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

}
