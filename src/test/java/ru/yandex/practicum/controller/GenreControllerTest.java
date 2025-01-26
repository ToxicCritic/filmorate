//package ru.yandex.practicum.controller;
//
//import lombok.RequiredArgsConstructor;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
//import org.springframework.context.annotation.Import;
//import ru.yandex.practicum.model.Genre;
//import ru.yandex.practicum.storage.genre.GenreDbStorage;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@JdbcTest
//@AutoConfigureTestDatabase
//@RequiredArgsConstructor(onConstructor_ = @Autowired)
//@Import({GenreDbStorage.class})
//public class GenreControllerTest {
//
//    @Autowired
//    private GenreDbStorage genreStorage;
//
//    @Test
//    public void testFindAllGenres() {
//        List<Genre> genres = genreStorage.findAll();
//
//        assertThat(genres).isNotEmpty();
//        assertThat(genres.get(0)).hasFieldOrPropertyWithValue("id", 3L)
//                .hasFieldOrPropertyWithValue("name", "Боевик");
//    }
//
//    @Test
//    public void testFindGenreById() {
//        Optional<Genre> genreOptional = genreStorage.findById(1L);
//
//        assertThat(genreOptional)
//                .isPresent()
//                .hasValueSatisfying(genre ->
//                        assertThat(genre).hasFieldOrPropertyWithValue("id", 1L)
//                                .hasFieldOrPropertyWithValue("name", "Комедия")
//                );
//    }
//}