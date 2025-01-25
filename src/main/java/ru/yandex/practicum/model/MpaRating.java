package ru.yandex.practicum.model;

public enum MpaRating {
    G,
    PG,
    PG_13,
    R,
    NC_17;

    public static MpaRating fromId(int id) {
        return values()[id - 1];
    }
}