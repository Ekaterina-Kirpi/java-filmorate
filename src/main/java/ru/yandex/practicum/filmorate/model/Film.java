package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import javax.validation.ValidationException;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Data
@Builder
@NoArgsConstructor

public class Film implements Serializable {

    private long id;
    @NotBlank(message = "Название не может быть пустым")
    private String name;
    @Size(message = "Максимальная длина описания - 200 символов")
    private String description;

    private LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма должна быть положительной")
    private Integer duration;
    private Mpa mpa;
    private Set<Genre> genres = new HashSet<>();
    private Set<Long> likes;

    public Film(long id, String name, String description, LocalDate releaseDate, Integer duration, Set<Long> likes) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.likes = likes == null ? new HashSet<>() : likes;

    }

    public Film(long id, String name, String description, LocalDate releaseDate, Integer duration, Mpa mpa, Set<Genre> genres, Set<Long> likes) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        this.genres = genres;
        this.likes = likes == null ? new HashSet<>() : likes;
    }

    public Film(String name, String description, LocalDate releaseDate, Integer duration) {
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