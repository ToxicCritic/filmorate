package ru.yandex.practicum.model;

import lombok.Data;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Data
public class User {
    private Long id;

    @Email(message = "Неверный формат электронной почты")
    @NotBlank(message = "Электронная почта не может быть пустой")
    private String email;

    @NotBlank(message = "Логин не может быть пустым")
    private String login;

    private String name;

    @Past(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;
}
