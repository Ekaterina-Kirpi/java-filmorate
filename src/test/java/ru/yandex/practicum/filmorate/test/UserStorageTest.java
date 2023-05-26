package ru.yandex.practicum.filmorate.test;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.UserDbStorage;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.filmorate.test.TestCreate.createUser1;
import static ru.yandex.practicum.filmorate.test.TestCreate.createUser2;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserStorageTest {
    private final UserDbStorage userDbStorage;
    private final JdbcTemplate jdbcTemplate;

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
    void getAll() {
        User user1 = createUser1();
        user1 = userDbStorage.createUser(user1);
        User user2 = createUser2();
        user2 = userDbStorage.createUser(user2);
        Collection<User> users = userDbStorage.getUsers();
        assertEquals(users.size(), 2);
        assertTrue(users.contains(user1));
        assertTrue(users.contains(user2));
    }

    @Test
    void getUser() {
        User user = createUser1();
        user = userDbStorage.createUser(user);
        User gottenUser = userDbStorage.getUserById(user.getId());
        assertEquals(user, gottenUser);
    }

    @Test
    void getFilmWithInvalidId() {
        ValidationException exception = assertThrows(ValidationException.class,
                () -> userDbStorage.getUserById(-1));
        assertEquals(exception.getStatus(), HttpStatus.NOT_FOUND);
    }

    @Test
    void addUser() {
        User user = createUser1();
        user = userDbStorage.createUser(user);
        Collection<User> users = userDbStorage.getUsers();
        assertEquals(users.size(), 1);
        assertTrue(users.contains(user));
    }

    @Test
    void updateUser() {
        User user = createUser1();
        user = userDbStorage.createUser(user);
        User updatedUser = createUser2();
        updatedUser.setId(user.getId());
        updatedUser = userDbStorage.updateUser(updatedUser);
        User finalUser = userDbStorage.getUserById(user.getId());
        assertEquals(updatedUser, finalUser);
    }

    @Test
    void updateNotExistingFilm() {
        User user = createUser1();
        ValidationException exception = assertThrows(ValidationException.class,
                () -> userDbStorage.updateUser(user));
        assertEquals(exception.getStatus(), HttpStatus.NOT_FOUND);
    }

}
