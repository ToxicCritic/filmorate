//package ru.yandex.practicum.controller;
//
//import lombok.RequiredArgsConstructor;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
//import org.springframework.context.annotation.Import;
//import ru.yandex.practicum.model.MpaRating;
//import ru.yandex.practicum.storage.mpa.MpaDbStorage;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@JdbcTest
//@AutoConfigureTestDatabase
//@RequiredArgsConstructor(onConstructor_ = @Autowired)
//@Import({MpaDbStorage.class})
//public class MpaControllerTest {
//
//    @Autowired
//    private MpaDbStorage mpaStorage;
//
//    @Test
//    public void testFindAllMpaRatings() {
//        List<MpaRating> ratings = mpaStorage.findAll();
//
//        assertThat(ratings).isNotEmpty();
//        assertThat(ratings.get(0)).hasFieldOrPropertyWithValue("id", 1)
//                .hasFieldOrPropertyWithValue("name", "G")
//                .hasFieldOrPropertyWithValue("description", "General Audiences");
//    }
//
//    @Test
//    public void testFindMpaRatingById() {
//        Optional<MpaRating> mpaRatingOptional = mpaStorage.findById(1L);
//
//        assertThat(mpaRatingOptional)
//                .isPresent()
//                .hasValueSatisfying(mpaRating ->
//                        assertThat(mpaRating).hasFieldOrPropertyWithValue("id", 1)
//                                .hasFieldOrPropertyWithValue("name", "G")
//                                .hasFieldOrPropertyWithValue("description", "General Audiences")
//                );
//    }
//}
