package ru.yandex.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Genre {
    private Long id;
    private String name;
}