package ru.yandex.practicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@Primary

public class UserDbStorage implements UserStorage {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserRowMapper userRowMapper;

    @Override
    public User createUser(User user) {
        validateUser(user);
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("USERS")
                .usingGeneratedKeyColumns("ID");
        Map<String, Object> values = new HashMap<>();
        values.put("email", user.getEmail());
        values.put("login", user.getLogin());
        values.put("name", user.getName());
        values.put("birthday", user.getBirthday());
        values.put("friends", user.getFriends());

        Number userKey = simpleJdbcInsert.executeAndReturnKey(new MapSqlParameterSource(values));
        user.setId(userKey.longValue());
        return user;
    }

    @Override
    public User updateUser(User user) {
        validateUser(user);
        String sql = "UPDATE USERS SET EMAIL = :email, LOGIN = :login, NAME = :name," +
                "BIRTHDAY = :birthday where id = :id";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("email", user.getEmail())
                .addValue("login", user.getLogin())
                .addValue("name", user.getName())
                .addValue("birthday", user.getBirthday())
                .addValue("id", user.getId());

        int count = namedParameterJdbcTemplate.update(sql, sqlParameterSource);
        if (count == 0) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "Пользователь с id " + user.getId() + " не найден");

        }
        return user;

    }

    @Override
    public List<User> getUsers() {
        String sqlAllUsers = "select * from users";
        return namedParameterJdbcTemplate.query(sqlAllUsers, userRowMapper);
    }

    @Override
    public User getUserById(long id) {
        String sqlUserById = "select * from users where users.id = ?";
        List<User> users = jdbcTemplate.query(sqlUserById, userRowMapper, id);
        if (users.isEmpty()) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "Пользователь не найден");
        }
        return users.get(0);
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
