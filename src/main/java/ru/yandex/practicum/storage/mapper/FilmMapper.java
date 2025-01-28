package ru.yandex.practicum.storage.mapper;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.dto.FilmDto;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.Genre;
import ru.yandex.practicum.model.MpaRating;
import ru.yandex.practicum.storage.genre.GenreStorage;
import ru.yandex.practicum.storage.mpa.MpaStorage;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FilmMapper {

    private static final Logger logger = LoggerFactory.getLogger(FilmMapper.class);

    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;

    public Film toFilm(FilmDto filmDto) {
        logger.info("Маппинг FilmDto в Film. mpaRatingId: {}", filmDto.getMpaRatingId());
        logger.info("Маппинг genreIds: {}", filmDto.getGenreIds());

        MpaRating mpaRating = mpaStorage.findById(filmDto.getMpaRatingId())
                .orElseThrow(() -> {
                    String message = "MPA рейтинг с ID " + filmDto.getMpaRatingId() + " не найден.";
                    logger.error(message);
                    return new NotFoundException(message);
                });

        logger.info("Получен MPA Rating: {}", mpaRating);

        Set<Genre> genres = null;
        if (filmDto.getGenreIds() != null && !filmDto.getGenreIds().isEmpty()) {
            genres = filmDto.getGenreIds().stream()
                    .map(id -> genreStorage.findById(id)
                            .orElseThrow(() -> {
                                String message = "Жанр с ID " + id + " не найден.";
                                logger.error(message);
                                return new NotFoundException(message);
                            }))
                    .collect(Collectors.toSet());
            logger.info("Получены жанры: {}", genres);
        }

        Film film = new Film(
                filmDto.getId(),
                filmDto.getName(),
                filmDto.getDescription(),
                filmDto.getReleaseDate(),
                filmDto.getDuration(),
                mpaRating,
                genres
        );

        logger.info("Результат маппинга FilmDto в Film: {}", film);
        return film;
    }

    public FilmDto toFilmDto(Film film) {
        Set<Integer> genreIds = null;
        if (film.getGenres() != null) {
            genreIds = film.getGenres().stream()
                    .map(Genre::getId)
                    .collect(Collectors.toSet());
        }

        FilmDto filmDto = new FilmDto(
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpaRating().getId(),
                genreIds
        );

        logger.info("Результат маппинга Film в FilmDto: {}", filmDto);
        return filmDto;
    }
}