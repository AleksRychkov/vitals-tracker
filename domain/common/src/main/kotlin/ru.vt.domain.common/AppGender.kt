package ru.vt.domain.common

enum class AppGender(val key: String) {
    UNDEFINED(""),
    MALE("Male"),
    FEMALE("Female"),
    OTHER("Other");

    companion object {
        fun valueFromKey(key: String): AppGender =
            values().firstOrNull { it.key.lowercase() == key.lowercase() } ?: UNDEFINED
    }
}
