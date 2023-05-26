package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.FriendshipDbStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.List;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;
    private final FriendshipDbStorage friendshipDbStorage;

    @Autowired
    public UserService(UserStorage userStorage, FriendshipDbStorage friendshipDbStorage) {
        this.userStorage = userStorage;
        this.friendshipDbStorage = friendshipDbStorage;
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
        if (getUserById(userId) == null || getUserById(friendId) == null) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "Пользователь не найден");
        }
        friendshipDbStorage.addToFriend(userId, friendId);
    }

    public void deleteFromFriend(long userId, long friendId) {
        friendshipDbStorage.deleteFromFriend(userId, friendId);
    }


    public List<User> getCommonFriends(long userId, long friendId) {
        return friendshipDbStorage.getCommonFriends(userId, friendId);
    }

    public List<User> getAllFriends(long id) {
        return friendshipDbStorage.getFriends(id);
    }

}