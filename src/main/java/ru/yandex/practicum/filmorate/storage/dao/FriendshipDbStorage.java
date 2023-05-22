package ru.yandex.practicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.FriendshipStorage;

import java.util.List;

@Slf4j
@Component
@Primary
public class FriendshipDbStorage implements FriendshipStorage {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private UserRowMapper userRowMapper;

    @Override
    public void addToFriend(long userId, long friendId) {
        String sqlToFriend = "INSERT INTO friendship (user_id, friend_id) VALUES (?, ?)";
        jdbcTemplate.update(sqlToFriend, userId, friendId);
    }

    @Override
    public void deleteFromFriend(long userId, long friendId) {
        String sqlFromFriend = "DELETE FROM friendship WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sqlFromFriend, userId, friendId);
    }

    @Override
    public List<User> getFriends(long userId) {
        String sqlAllFriends = "SELECT * " +
                "FROM users " +
                "WHERE id IN (SELECT FRIEND_ID FROM FRIENDSHIP WHERE USER_ID = ?)";
        List<User> users = jdbcTemplate.query(sqlAllFriends, userRowMapper, userId);
        return users;
    }

    @Override
    public List<User> getCommonFriends(long userId, long friendId) {
        String sql = "SELECT users.* " +
                "FROM users " +
                "JOIN FRIENDSHIP AS f1 on(users.id = f1.friend_id AND f1.user_id = ?) " +
                "JOIN FRIENDSHIP AS f2 on (users.id = f2.friend_id AND f2.user_id =?)";
        return jdbcTemplate.query(sql, userRowMapper, userId, friendId);
    }
}