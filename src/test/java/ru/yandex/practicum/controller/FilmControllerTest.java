package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.dto.FilmDto;
import ru.yandex.practicum.service.FilmService;
import ru.yandex.practicum.storage.film.FilmDbStorage;
import ru.yandex.practicum.storage.genre.GenreDbStorage;
import ru.yandex.practicum.storage.like.LikeDbStorage;
import ru.yandex.practicum.storage.mapper.FilmMapper;
import ru.yandex.practicum.storage.mpa.MpaDbStorage;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@JdbcTest
@AutoConfigureTestDatabase(replace = NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({
        FilmController.class,
        FilmService.class,
        FilmMapper.class,
        FilmDbStorage.class,
        GenreDbStorage.class,
        MpaDbStorage.class,
        LikeDbStorage.class
})
@AutoConfigureWebMvc
class FilmControllerTest {

    private final FilmController filmController;

    @Test
    @DisplayName("Создание нового фильма")
    void createFilmTest() {
        FilmDto filmDto = new FilmDto();
        filmDto.setName("Inception");
        filmDto.setDescription("A mind-bending thriller about dream invasion.");
        filmDto.setReleaseDate(LocalDate.of(2010, 7, 16));
        filmDto.setDuration(148);
        filmDto.setMpaRatingId(1);
        filmDto.setGenreIds(Set.of(1, 2));

        FilmDto created = filmController.createFilm(filmDto);

        assertNotNull(created.getId(), "ID фильма после создания не должен быть null");
        assertEquals("Inception", created.getName());
        assertEquals(148, created.getDuration());
        assertEquals(1, created.getMpaRatingId());
        assertEquals(Set.of(1, 2), created.getGenreIds());
    }

    @Test
    @DisplayName("Обновление существующего фильма")
    void updateFilmTest() {
        FilmDto filmDto = new FilmDto();
        filmDto.setName("Initial Film");
        filmDto.setDescription("Desc");
        filmDto.setReleaseDate(LocalDate.of(2020, 1, 1));
        filmDto.setDuration(100);
        filmDto.setMpaRatingId(1);
        filmDto.setGenreIds(Set.of(2));

        FilmDto created = filmController.createFilm(filmDto);
        Integer newId = created.getId();

        created.setName("Updated Film");
        created.setDuration(120);

        FilmDto updated = filmController.updateFilm(created);

        assertEquals(newId, updated.getId(), "ID фильма не должен меняться при обновлении");
        assertEquals("Updated Film", updated.getName());
        assertEquals(120, updated.getDuration());
    }

    @Test
    @DisplayName("Получение всех фильмов (пустой список)")
    void getAllFilmsEmpty() {
        var all = filmController.getAllFilms();
        assertNotNull(all);
        assertFalse(all.isEmpty(), "Список фильмов ожидается пустым");
    }

    @Test
    @DisplayName("Получение фильма по ID (не найден)")
    void getFilmByIdNotFound() {
        assertThrows(
                ru.yandex.practicum.exception.NotFoundException.class,
                () -> filmController.getFilmById(999),
                "Ожидаем NotFoundException, если фильм не найден"
        );
    }
}