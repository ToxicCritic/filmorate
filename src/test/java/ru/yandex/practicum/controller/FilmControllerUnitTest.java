package ru.yandex.practicum.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.exception.ValidationException;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.service.FilmService;
import ru.yandex.practicum.storage.film.InMemoryFilmStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class FilmControllerUnitTest {

    private FilmController filmController;

    @BeforeEach
    public void setup() {
        FilmService filmService = new FilmService(new InMemoryFilmStorage());
        filmController = new FilmController(filmService);
    }

    @Test
    public void shouldCreateFilmSuccessfully() {
        Film film = new Film();
        film.setName("New Film");
        film.setDescription("A great film");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);

        Film createdFilm = filmController.addFilm(film);

        assertNotNull(createdFilm);
        assertEquals(1, createdFilm.getId());
        assertEquals("New Film", createdFilm.getName());
    }

    @Test
    public void shouldUpdateFilmSuccessfully() {
        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("Test Description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);

        Film createdFilm = filmController.addFilm(film);

        createdFilm.setName("Updated Film");
        Film updatedFilm = filmController.updateFilm(createdFilm);

        assertNotNull(updatedFilm);
        assertEquals(createdFilm.getId(), updatedFilm.getId());
        assertEquals("Updated Film", updatedFilm.getName());
    }

    @Test
    public void shouldDeleteFilmSuccessfully() {
        Film film = new Film();
        film.setName("Film to Delete");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);

        Film createdFilm = filmController.addFilm(film);

        assertDoesNotThrow(() -> filmController.deleteFilm(createdFilm));

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            filmController.getFilmById(createdFilm.getId());
        });

        assertEquals("Фильм с таким ID не найден.", exception.getMessage());
    }

    @Test
    public void shouldGetFilmByIdSuccessfully() {
        Film film = new Film();
        film.setName("Get Film");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);

        Film createdFilm = filmController.addFilm(film);

        Film retrievedFilm = filmController.getFilmById(createdFilm.getId());

        assertNotNull(retrievedFilm);
        assertEquals(createdFilm.getId(), retrievedFilm.getId());
        assertEquals("Get Film", retrievedFilm.getName());
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
}