package ru.yandex.practicum.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.model.MpaRating;
import ru.yandex.practicum.storage.mpa.MpaStorage;

import java.util.List;

@Service
public class MpaService {

    private static final Logger logger = LoggerFactory.getLogger(MpaService.class);

    private final MpaStorage mpaStorage;

    public MpaService(MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public List<MpaRating> getAllMpaRatings() {
        logger.info("Запрос на получение всех MPA рейтингов");
        return mpaStorage.findAll();
    }

    public MpaRating getMpaRatingById(Integer id) {
        logger.info("Запрос на получение MPA рейтинга с ID: {}", id);
        return mpaStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("MPA рейтинг с ID " + id + " не найден."));
    }
}