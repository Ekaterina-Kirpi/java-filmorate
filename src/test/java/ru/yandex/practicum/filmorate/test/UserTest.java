package ru.yandex.practicum.filmorate.test;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import javax.xml.validation.Validator;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserTest {
    private User user;
    private Validator validator;
    private UserService userService;

    @BeforeEach
    void beforeEach() {
        userService = new UserService(new InMemoryUserStorage());
        user = new User(1, "mail@mail.ru", "dolore", "Nick Name",
                LocalDate.of(1895, 12, 28), null);
    }

    @Test
    void shouldCreate() {
        userService.createUser(user);
        List<User> userList = userService.getUsers();
        assertEquals(1, userList.size(), "Id не совпадает");
        assertEquals(user.getEmail(), userList.get(0).getEmail(), "Некорректный адрес электронной почты");
        assertEquals(user.getName(), userList.get(0).getName(), "Некорректное имя пользователя");
        assertEquals(user.getLogin(), userList.get(0).getLogin(), "Некорректный логин");
        assertEquals(user.getBirthday(), userList.get(0).getBirthday(), "Некорректная дата рождения");
    }

    @Test
    void shouldUpdate() {
        userService.createUser(user);
        user.setName("Maxim");
        user.setEmail("max@mail.ru");
        user.setBirthday(LocalDate.of(2000, 1, 1));
        user.setLogin("Max");
        userService.updateUser(user);
        List<User> userList = userService.getUsers();
        assertEquals(1, userList.size(), "Id не совпадает");
        assertEquals(user.getEmail(), userList.get(0).getEmail(), "Некорректный адрес электронной почты");
        assertEquals(user.getName(), userList.get(0).getName(), "Неккоректное имя подьзователя");
        assertEquals(user.getLogin(), userList.get(0).getLogin(), "Неккоректный логин");
        assertEquals(user.getBirthday(), userList.get(0).getBirthday(), "Некорректная дата рождения");
    }

    @Test
    void shouldGetAllUsers() {
        userService.createUser(user);
        User newUser = new User(2, "maxi@mail.ru", "Maxi", "Maxim",
                LocalDate.of(2001, 1, 1), null);
        userService.createUser(newUser);
        List<User> userList = userService.getUsers();
        assertEquals(2, userList.size());
        assertTrue(userList.contains(user));
        assertTrue(userList.contains(newUser));
    }

    @Test
    void shouldValidationEmail() {
        userService.createUser(user);
        user.setEmail("max.ru");
        assertThrows(ValidationException.class, () -> {
            userService.validateUser(user);
        });
        user.setEmail("null");
        assertThrows(ValidationException.class, () -> {
            userService.validateUser(user);
        });
    }

    @Test
    void shouldValidationLogin() {
        userService.createUser(user);
        user.setLogin("  ");
        assertThrows(ValidationException.class, () -> {
            userService.validateUser(user);
        });
    }

    @Test
    void shouldValidationName() {
        userService.createUser(user);
        user.setName("");
        userService.validateUser(user);
        assertEquals(user.getLogin(), user.getName());
    }

    @Test
    void shouldValidationBirthday() {
        userService.createUser(user);
        user.setBirthday(LocalDate.now().plusDays(7));
        assertThrows(ValidationException.class, () -> {
            userService.validateUser(user);
        });
    }
}
