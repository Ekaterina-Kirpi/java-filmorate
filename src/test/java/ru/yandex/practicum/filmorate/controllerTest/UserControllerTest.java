package ru.yandex.practicum.filmorate.controllerTest;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ValidationException;
import javax.xml.validation.Validator;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserControllerTest {
    private UserController userController;
    private User user;
    private Validator validator;

    @BeforeEach
    void beforeEach() {
        userController = new UserController();
        user = new User(1, "mail@mail.ru", "dolore", "Nick Name",
                LocalDate.of(1895, 12, 28));
    }

    @Test
    void shouldCreate() {
        userController.create(user);
        List<User> userList = userController.getUsers();
        assertEquals(1, userList.size(), "Id не совпадает");
        assertEquals(user.getEmail(), userList.get(0).getEmail(), "Некорректный адрес электронной почты");
        assertEquals(user.getName(), userList.get(0).getName(), "Некорректное имя пользователя");
        assertEquals(user.getLogin(), userList.get(0).getLogin(), "Некорректный логин");
        assertEquals(user.getBirthday(), userList.get(0).getBirthday(), "Некорректная дата рождения");
    }

    @Test
    void shouldUpdate() {
        userController.create(user);
        user.setName("Maxim");
        user.setEmail("max@mail.ru");
        user.setBirthday(LocalDate.of(2000, 1, 1));
        user.setLogin("Max");
        userController.update(user);
        List<User> userList = userController.getUsers();
        assertEquals(1, userList.size(), "Id не совпадает");
        assertEquals(user.getEmail(), userList.get(0).getEmail(), "Некорректный адрес электронной почты");
        assertEquals(user.getName(), userList.get(0).getName(), "Неккоректное имя подьзователя");
        assertEquals(user.getLogin(), userList.get(0).getLogin(), "Неккоректный логин");
        assertEquals(user.getBirthday(), userList.get(0).getBirthday(), "Некорректная дата рождения");
    }

    @Test
    void shouldGetAllUsers() {
        userController.create(user);
        User newUser = new User(2, "maxi@mail.ru", "Maxi", "Maxim", LocalDate.of(2001, 1, 1));
        userController.create(newUser);
        List<User> userList = userController.getUsers();
        assertEquals(2, userList.size());
        assertTrue(userList.contains(user));
        assertTrue(userList.contains(newUser));
    }

    @Test
    void shouldValidationEmail() {
        userController.create(user);
        user.setEmail("max.ru");
        assertThrows(ValidationException.class, () -> {
            userController.validateUser(user);
        });
        user.setEmail("null");
        assertThrows(ValidationException.class, () -> {
            userController.validateUser(user);
        });
    }

    @Test
    void shouldValidationLogin() {
        userController.create(user);
        user.setLogin("  ");
        assertThrows(ValidationException.class, () -> {
            userController.validateUser(user);
        });
    }

    @Test
    void shouldValidationName() {
        userController.create(user);
        user.setName("");
        userController.validateUser(user);
        assertEquals(user.getLogin(), user.getName());
    }

    @Test
    void shouldValidationBirthday() {
        userController.create(user);
        user.setBirthday(LocalDate.now().plusDays(7));
        assertThrows(ValidationException.class, () -> {
            userController.validateUser(user);
        });
    }
}