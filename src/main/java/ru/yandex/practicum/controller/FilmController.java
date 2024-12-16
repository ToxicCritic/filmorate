package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import ru.yandex.practicum.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.model.Film;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
@Validated
public class FilmController {

    private final List<Film> films = new ArrayList<>();

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film addFilm(@Valid @RequestBody Film film) {
        if (film.getReleaseDate().isBefore(java.time.LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года.");
        }
        film.setId(films.size() + 1);
        films.add(film);
        log.info("Фильм добавлен: {}", film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (film.getId() <= 0 || film.getId() > films.size()) {
            throw new ValidationException("Фильм с таким ID не найден.");
        }
        films.set(film.getId() - 1, film);
        log.info("Фильм обновлен: {}", film);
        return film;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return films;
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleValidationException(ValidationException e) {
        log.error("Ошибка валидации: {}", e.getMessage());
        return e.getMessage();
    }
}
