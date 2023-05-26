package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Builder
@Data
@NoArgsConstructor
public class User {
    private long id;
    @Email(message = "Адрес электронной почты должен содеражать @")
    @NotBlank(message = "Укажите адрес электронной почты, поле не должно быть пустым")
    private String email;
    @NotNull(message = "Логин не может быть пустым или содержаит пробелы")
    private String login;
    private String name;
    @NotNull(message = "Нужно указать дату рождения, поле не может быть пустым")
    @Past(message = "Не верно указана дата, это время еще не наступило")
    private LocalDate birthday;
    private Set<Long> friends;

    public User(long id, String email, String login, String name, LocalDate birthday, Set<Long> friends) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
        this.friends = friends == null ? new HashSet<>() : friends;
    }

    public User(String login, String name, String email, LocalDate birthday) {
        this.login = login;
        this.name = name;
        this.email = email;
        this.birthday = birthday;
    }
}