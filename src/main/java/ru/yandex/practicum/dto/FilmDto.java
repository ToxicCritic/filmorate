package ru.yandex.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class FilmDto {
    private Integer id;

    @NotBlank(message = "Название фильма не может быть пустым.")
    private String name;

    @Size(max = 200, message = "Описание фильма не может превышать 200 символов.")
    private String description;

    @NotNull(message = "Дата релиза фильма не может быть пустой.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;

    @NotNull(message = "Продолжительность фильма не может быть пустой.")
    @Min(value = 1, message = "Продолжительность фильма должна быть положительной.")
    private Integer duration;

    @NotNull(message = "Рейтинг MPA не может быть пустой.")
    private Integer mpaRatingId;

    private Set<Integer> genreIds;
}