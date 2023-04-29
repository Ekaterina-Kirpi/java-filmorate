package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ValidationException;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Builder
@Data

public class Film {

    private long id;
    @NotBlank(message = "Название не может быть пустым")
    private String name;
    @Size(message = "Максимальная длина описания - 200 символов")
    private String description;

    private LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма должна быть положительной")
    private Integer duration;
    private Set<Long> likes;

    public Film(long id, String name, String description, LocalDate releaseDate, Integer duration, Set<Long> likes) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.likes = likes == null ? new HashSet<>() : likes;
    }

    public void addLike(long userId) {
        likes.add(userId);
    }

    public void removeLike(long userId) {
        if (!likes.contains(userId)) {
            log.warn("Лайк уже удален");
            throw new ValidationException("Лайк уже удален");
        }
        likes.remove(userId);
    }


}