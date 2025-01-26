package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.FilmDto;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.service.FilmService;
import ru.yandex.practicum.storage.mapper.FilmMapper;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/films")
@Validated
@RequiredArgsConstructor
@Slf4j
public class FilmController {
    private final FilmService filmService;
    private final FilmMapper filmMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FilmDto createFilm(@Valid @RequestBody FilmDto filmDto) {
        log.info("Получен запрос на создание фильма: {}", filmDto);

        if (filmDto.getMpaRatingId() == null) {
            log.error("Поле mpaRatingId отсутствует или равно null.");
        } else {
            log.info("Получен mpaRatingId: {}", filmDto.getMpaRatingId());
        }

        Film film = filmMapper.toFilm(filmDto);
        Film createdFilm = filmService.createFilm(film);
        log.info("Фильм успешно создан: {}", createdFilm);
        return filmMapper.toFilmDto(createdFilm);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public FilmDto updateFilm(@Valid @RequestBody FilmDto filmDto) {
        log.info("Получен запрос на обновление фильма: {}", filmDto);
        Film film = filmMapper.toFilm(filmDto);
        Film updatedFilm = filmService.updateFilm(film);
        log.info("Фильм успешно обновлён: {}", updatedFilm);
        return filmMapper.toFilmDto(updatedFilm);
    }


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<FilmDto> getAllFilms() {
        log.info("Получение всех фильмов.");
        List<Film> films = filmService.getAllFilms();
        return films.stream()
                .map(filmMapper::toFilmDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public FilmDto getFilmById(@PathVariable Integer id) {
        log.info("Получение фильма с ID: {}", id);
        Film film = filmService.getFilmById(id)
                .orElseThrow(() -> {
                    String message = "Фильм с ID " + id + " не найден.";
                    log.error(message);
                    return new NotFoundException(message);
                });
        return filmMapper.toFilmDto(film);
    }

    @PutMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void likeFilm(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("Добавление лайка фильму с ID: {} от пользователя с ID: {}", id, userId);
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void unlikeFilm(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("Удаление лайка фильму с ID: {} от пользователя с ID: {}", id, userId);
        filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    @ResponseStatus(HttpStatus.OK)
    public List<FilmDto> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("Получение {} популярных фильмов.", count);
        List<Film> popularFilms = filmService.getPopularFilms(count);
        return popularFilms.stream()
                .map(filmMapper::toFilmDto)
                .collect(Collectors.toList());
    }
}