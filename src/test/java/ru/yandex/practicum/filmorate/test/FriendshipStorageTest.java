package ru.yandex.practicum.filmorate.test;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.FriendshipDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.UserDbStorage;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.yandex.practicum.filmorate.test.TestCreate.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FriendshipStorageTest {

    private final JdbcTemplate jdbcTemplate;
    private final FriendshipDbStorage friendshipDbStorage;
    private final UserDbStorage userDbStorage;

    @BeforeEach
    void clearDb() {
        String sql = "DELETE FROM likes";
        jdbcTemplate.update(sql);
        sql = "DELETE FROM friendship";
        jdbcTemplate.update(sql);
        sql = "DELETE FROM users";
        jdbcTemplate.update(sql);
    }

    @Test
    void addFriend() {
        User user1 = createUser1();
        user1 = userDbStorage.createUser(user1);
        User user2 = createUser2();
        user2 = userDbStorage.createUser(user2);
        friendshipDbStorage.addToFriend(user1.getId(), user2.getId());
        List<User> friends = friendshipDbStorage.getFriends(user1.getId());
        assertEquals(friends.size(), 1);
        assertTrue(friends.contains(user2));
    }

    @Test
    void deleteFriend() {
        User user1 = createUser1();
        user1 = userDbStorage.createUser(user1);
        User user2 = createUser2();
        user2 = userDbStorage.createUser(user2);
        friendshipDbStorage.addToFriend(user1.getId(), user2.getId());
        friendshipDbStorage.deleteFromFriend(user1.getId(), user2.getId());
        List<User> friends = friendshipDbStorage.getFriends(user1.getId());
        assertTrue(friends.isEmpty());
    }

    @Test
    void getFriend() {
        User user1 = createUser1();
        user1 = userDbStorage.createUser(user1);
        User user2 = createUser2();
        user2 = userDbStorage.createUser(user2);
        User user3 = createUser3();
        user3 = userDbStorage.createUser(user3);
        friendshipDbStorage.addToFriend(user1.getId(), user2.getId());
        friendshipDbStorage.addToFriend(user1.getId(), user3.getId());
        Collection<User> friends = friendshipDbStorage.getFriends(user1.getId());
        assertEquals(friends.size(), 2);
        assertTrue(friends.contains(user2));
        assertTrue(friends.contains(user3));
    }

    @Test
    void getCommonFriends() {
        User user1 = createUser1();
        user1 = userDbStorage.createUser(user1);
        User user2 = createUser2();
        user2 = userDbStorage.createUser(user2);
        User user3 = createUser3();
        user3 = userDbStorage.createUser(user3);
        friendshipDbStorage.addToFriend(user1.getId(), user3.getId());
        friendshipDbStorage.addToFriend(user2.getId(), user3.getId());
        Collection<User> friends = friendshipDbStorage.getCommonFriends(user1.getId(), user2.getId());
        assertEquals(friends.size(), 1);
        assertTrue(friends.contains(user3));
    }

}