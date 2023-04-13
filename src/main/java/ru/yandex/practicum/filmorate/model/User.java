package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.Objects;

@Slf4j
@Data
@Builder

public class User {

    private int id;
    @Email(message = "Адрес электронной почты должен содеражать @")
    @NotBlank(message = "Укажите адрес электронной почты, поле не должно быть пустым")
    private String email;
    @NotNull(message = "Логин не может быть пустым или содержаит пробелы")
    private String login;
    private String name;
    @NotNull(message = "Нужно указать дату рождения, поле не может быть пустым")
    @Past(message = "Не верно указана дата, это время еще не наступило")
    private LocalDate birthday;

    public User(int id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

}
