package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        long id = rs.getInt("users.id");
        String name = rs.getString("users.name");
        String login = rs.getString("users.login");
        String email = rs.getString("users.email");
        LocalDate birthday = rs.getDate("users.birthday").toLocalDate();
        User user = new User(login, name, email, birthday);
        user.setId(id);
        return user;
    }
}