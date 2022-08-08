package ru.vt.core.common.extension


fun <T> Result<T>.rigidSuccess(): Boolean = this.isSuccess && this.getOrNull() != null