package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.model.Genre;
import ru.yandex.practicum.service.GenreService;
import ru.yandex.practicum.storage.genre.GenreDbStorage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase(replace = NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({
        GenreController.class,
        GenreService.class,
        GenreDbStorage.class
})
@AutoConfigureWebMvc
class GenreControllerTest {

    private final GenreController genreController;

    @Test
    @DisplayName("Поиск жанра по несуществующему ID должен бросать NotFoundException")
    void getGenreByIdNotFound() {
        assertThrows(
                NotFoundException.class,
                () -> genreController.getGenreById(999),
                "Ожидаем, что при несуществующем ID бросится NotFoundException"
        );
    }

    @Test
    @DisplayName("Поиск жанра по ID")
    void getGenreByIdFound() {
        Genre found = genreController.getGenreById(1);
        assertNotNull(found, "Найденный жанр не должен быть null");
        assertEquals(1, found.getId(), "ID жанра должен совпадать");
        assertEquals("Комедия", found.getName(), "Название жанра должно совпадать");
    }

    @Test
    @DisplayName("Получение всех жанров")
    void getAllGenres() {
        List<Genre> genres = genreController.getAllGenres();
        assertNotNull(genres, "Список жанров не должен быть null");
        assertEquals(8, genres.size(), "Ожидаем 8 жанров");
    }
}