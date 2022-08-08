package ru.vt.domain.measurement.exceptions

class ValueRangeException(
    val key: String,
    val range: Pair<Int, Int>
) : IllegalArgumentException()