package ru.yandex.practicum.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.model.MpaRating;
import ru.yandex.practicum.storage.mpa.MpaStorage;

import java.util.List;
import java.util.Optional;

@Service
public class MpaService {

    private final MpaStorage mpaStorage;

    public MpaService(MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public List<MpaRating> getAllMpaRatings() {
        return mpaStorage.findAll();
    }

    public Optional<MpaRating> getMpaRatingById(Long id) {
        return mpaStorage.findById(id);
    }
}