package ru.vt.domain.common

enum class MeasurementParams(val key: String) {
    HEART_RATE("heart_rate"),
    SYSTOLIC("systolic"),
    DIASTOLIC("diastolic"),

    HEADACHE_INTENSITY("headache_intensity"),
    HEADACHE_ALL("headache_area_all"),
    HEADACHE_LEFT("headache_area_left"),
    HEADACHE_RIGHT("headache_area_right"),
    HEADACHE_FOREHEAD("headache_area_forehead"),
    HEADACHE_BACK_HEAD("headache_area_back_head"),

    WEIGHT("weight"),
}