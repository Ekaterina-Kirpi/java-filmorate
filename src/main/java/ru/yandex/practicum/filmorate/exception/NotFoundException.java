package ru.yandex.practicum.filmorate.exception;

public class NotFoundException extends RuntimeException{
    public NotFoundException(final String e){
        super(e);
    }
}
