package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.storage.film.FilmStorage;
import ru.yandex.practicum.storage.like.LikeStorage;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FilmService {

    private static final Logger logger = LoggerFactory.getLogger(FilmService.class);

    private final FilmStorage filmStorage;
    private final LikeStorage likeStorage;

    @Transactional
    public Film createFilm(Film film) {
        logger.info("Создание нового фильма: {}", film.getName());
        Film savedFilm = filmStorage.save(film);
        logger.info("Фильм успешно сохранён: {}", savedFilm);
        return savedFilm;
    }

    @Transactional
    public Film updateFilm(Film film) {
        logger.info("Обновление фильма с ID: {}", film.getId());
        Film updatedFilm = filmStorage.update(film);
        logger.info("Фильм успешно обновлён: {}", updatedFilm);
        return updatedFilm;
    }

    public List<Film> getAllFilms() {
        logger.info("Получение списка всех фильмов.");
        return filmStorage.findAll();
    }

    public Optional<Film> getFilmById(Integer id) {
        logger.info("Получение фильма с ID: {}", id);
        return filmStorage.findById(id);
    }

    @Transactional
    public void addLike(Integer filmId, Integer userId) {
        logger.info("Добавление лайка фильму с ID: {} от пользователя с ID: {}", filmId, userId);
        // Проверка существования фильма
        filmStorage.findById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм с ID " + filmId + " не найден."));
        likeStorage.addLike(filmId, userId);
        logger.info("Лайк успешно добавлен.");
    }

    @Transactional
    public void removeLike(Integer filmId, Integer userId) {
        logger.info("Удаление лайка фильму с ID: {} от пользователя с ID: {}", filmId, userId);
        // Проверка существования фильма
        filmStorage.findById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм с ID " + filmId + " не найден."));
        likeStorage.removeLike(filmId, userId);
        logger.info("Лайк успешно удалён.");
    }

    public List<Film> getPopularFilms(int count) {
        logger.info("Получение {} популярных фильмов.", count);
        List<Film> popularFilms = likeStorage.getPopularFilms(count);
        logger.info("Найдено {} популярных фильмов.", popularFilms.size());
        return popularFilms;
    }
}