package ru.yandex.practicum.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.MpaRating;
import ru.yandex.practicum.service.MpaService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/mpa")
public class MpaController {

    private final MpaService mpaService;

    public MpaController(MpaService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping
    public ResponseEntity<List<MpaRating>> getAllMpaRatings() {
        return ResponseEntity.ok(mpaService.getAllMpaRatings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MpaRating> getMpaRatingById(@PathVariable Long id) {
        Optional<MpaRating> mpaRating = mpaService.getMpaRatingById(id);
        return mpaRating.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}