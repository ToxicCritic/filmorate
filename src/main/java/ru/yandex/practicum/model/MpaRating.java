package ru.yandex.practicum.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MpaRating {
    private Integer id;
    @NotBlank(message = "Название MPA рейтинга не может быть пустым.")
    private String name;
    private String description;

    public MpaRating(int id) {
        this.id = id;
    }
}