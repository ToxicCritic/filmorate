package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.model.MpaRating;
import ru.yandex.practicum.service.MpaService;
import ru.yandex.practicum.storage.mpa.MpaDbStorage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@JdbcTest
@AutoConfigureTestDatabase(replace = NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({
        MpaController.class,
        MpaService.class,
        MpaDbStorage.class
})
@AutoConfigureWebMvc
class MpaControllerTest {

    private final MpaController mpaController;

    @Test
    @DisplayName("Получение MPA по несуществующему ID должно бросать NotFoundException")
    void getMpaRatingByIdNotFound() {
        assertThrows(
                NotFoundException.class,
                () -> mpaController.getMpaRatingById(999),
                "Ожидаем NotFoundException при запросе несуществующего ID"
        );
    }

    @Test
    @DisplayName("Получение MPA по существующему ID после вставки")
    void getMpaRatingByIdSuccess() {
        MpaRating found = mpaController.getMpaRatingById(1);
        assertNotNull(found, "Найденный MPA Rating не должен быть null");
        assertEquals(1, found.getId());
        assertEquals("G", found.getName());
        assertEquals("General Audiences", found.getDescription());
    }

    @Test
    @DisplayName("Список MPA после вставки нескольких записей")
    void getAllMpaRatingsWithData() {
        List<MpaRating> ratings = mpaController.getAllMpaRatings();
        assertNotNull(ratings, "Список MPA не должен быть null");
        assertEquals(5, ratings.size(), "Ожидаем 5 записей в MPA Ratings");

        assertTrue(ratings.stream().anyMatch(mpa -> mpa.getId() == 5 && "NC-17".equals(mpa.getName())));
        assertTrue(ratings.stream().anyMatch(mpa -> mpa.getId() == 3 && "PG-13".equals(mpa.getName())));
    }
}