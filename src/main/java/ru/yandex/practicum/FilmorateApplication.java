package ru.yandex.practicum;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FilmorateApplication {
    @Value("${test.property:Configuration not loaded}")
    private String testProperty;

    public static void main(String[] args) {
        SpringApplication.run(FilmorateApplication.class, args);
    }

    @PostConstruct
    public void verifyConfiguration() {
        System.out.println("Test Property: " + testProperty);
    }
}