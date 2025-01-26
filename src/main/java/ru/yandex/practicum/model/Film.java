package ru.yandex.practicum.model;

import lombok.Data;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;


@Data
public class Film {
    private Long id;
    @NotBlank(message = "Название не может быть пустым")
    private String name;
    @Size(max = 200, message = "Описание не может превышать 200 символов")
    private String description;
    @NotNull(message = "Дата релиза не может быть пустой")
    @PastOrPresent(message = "Дата релиза не может быть в будущем")
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность должна быть положительным числом")
    private int duration;
    @NotNull(message = "Рейтинг MPA не может быть пустым")
    private int mpaRatingId;
    @NotEmpty(message = "Фильму должен быть присвоен хотя бы один жанр")
    private List<Integer> genreIds;
}

