package ru.yandex.practicum.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.model.Genre;
import ru.yandex.practicum.storage.genre.GenreStorage;

import java.util.List;
import java.util.Optional;

@Service
public class GenreService {

    private final GenreStorage genreStorage;

    public GenreService(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public List<Genre> getAllGenres() {
        return genreStorage.findAll();
    }

    public Optional<Genre> getGenreById(Long id) {
        return genreStorage.findById(id);
    }
}