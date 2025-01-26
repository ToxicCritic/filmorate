//package ru.yandex.practicum.controller;
//
//import lombok.RequiredArgsConstructor;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
//import org.springframework.context.annotation.Import;
//import ru.yandex.practicum.model.Film;
//import ru.yandex.practicum.storage.film.FilmDbStorage;
//
//import java.time.LocalDate;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@JdbcTest
//@AutoConfigureTestDatabase
//@RequiredArgsConstructor(onConstructor_ = @Autowired)
//@Import({FilmDbStorage.class})
//public class FilmControllerTest {
//
//    @Autowired
//    private FilmDbStorage filmStorage;
//
//    @Test
//    public void testFindFilmById() {
//        Optional<Film> filmOptional = filmStorage.findById(1L);
//
//        assertThat(filmOptional)
//                .isPresent()
//                .hasValueSatisfying(film ->
//                        assertThat(film).hasFieldOrPropertyWithValue("id", 1L)
//                                .hasFieldOrPropertyWithValue("name", "Inception")
//                                .hasFieldOrPropertyWithValue("description", "A mind-bending thriller")
//                                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(2010, 7, 16))
//                                .hasFieldOrPropertyWithValue("duration", 148)
//                                .hasFieldOrPropertyWithValue("mpaRatingId", 2)
//                );
//    }
//
//    @Test
//    public void testSaveFilm() {
//        Film newFilm = new Film();
//        newFilm.setName("Interstellar");
//        newFilm.setDescription("A journey through space and time");
//        newFilm.setReleaseDate(LocalDate.of(2014, 11, 7));
//        newFilm.setDuration(169);
//        //newFilm.setMpaRatingId(2);
//
//        Film savedFilm = filmStorage.save(newFilm);
//
//        assertThat(savedFilm).isNotNull();
//        assertThat(savedFilm.getId()).isNotNull();
//
//        Optional<Film> retrievedFilm = filmStorage.findById(savedFilm.getId());
//
//        assertThat(retrievedFilm)
//                .isPresent()
//                .hasValueSatisfying(film ->
//                        assertThat(film).hasFieldOrPropertyWithValue("name", "Interstellar")
//                                .hasFieldOrPropertyWithValue("description", "A journey through space and time")
//                                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(2014, 11, 7))
//                                .hasFieldOrPropertyWithValue("duration", 169)
//                                .hasFieldOrPropertyWithValue("mpaRatingId", 2)
//                );
//    }
//
//    @Test
//    public void testUpdateFilm() {
//        Film existingFilm = filmStorage.findById(1L).orElseThrow(() -> new RuntimeException("Film not found"));
//        existingFilm.setName("Inception Updated");
//
//        filmStorage.update(existingFilm);
//
//        Optional<Film> updatedFilm = filmStorage.findById(1L);
//
//        assertThat(updatedFilm)
//                .isPresent()
//                .hasValueSatisfying(film ->
//                        assertThat(film).hasFieldOrPropertyWithValue("name", "Inception Updated")
//                );
//    }
//
//    @Test
//    public void testDeleteFilm() {
//        filmStorage.delete(1L);
//
//        Optional<Film> deletedFilm = filmStorage.findById(1L);
//
//        assertThat(deletedFilm).isNotPresent();
//    }
//}