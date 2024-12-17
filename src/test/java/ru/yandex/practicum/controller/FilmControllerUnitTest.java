package ru.yandex.practicum.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.exception.ValidationException;
import ru.yandex.practicum.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class FilmControllerUnitTest {

    private FilmController filmController;

    @BeforeEach
    public void setup() {
        filmController = new FilmController();
    }

    @Test
    public void shouldAddFilmSuccessfully() {
        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("Test Description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);

        Film createdFilm = filmController.addFilm(film);

        assertNotNull(createdFilm);
        assertEquals(1, createdFilm.getId());
        assertEquals("Test Film", createdFilm.getName());
    }

    @Test
    public void shouldThrowExceptionForInvalidReleaseDate() {
        Film film = new Film();
        film.setName("Invalid Film");
        film.setDescription("Invalid Description");
        film.setReleaseDate(LocalDate.of(1800, 1, 1));
        film.setDuration(120);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            filmController.addFilm(film);
        });

        assertEquals("Дата релиза не может быть раньше 28 декабря 1895 года.", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionForUpdatingNonExistentFilm() {
        Film film = new Film();
        film.setId(999L);
        film.setName("Non-existent Film");

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            filmController.updateFilm(film);
        });

        assertEquals("Фильм с таким ID не найден.", exception.getMessage());
    }
}
