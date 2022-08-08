package ru.vt.core.common.extension

fun <T> lazyNone(block: () -> T): Lazy<T> = lazy(LazyThreadSafetyMode.NONE) { block() }

inline fun <reified T : Enum<T>> valueOf(type: String?, default: T): T {
    if (type == null) return default
    return try {
        java.lang.Enum.valueOf(T::class.java, type)
    } catch (e: Exception) {
        default
    }
}

inline fun <reified T : Enum<T>> valueOf(type: String?): T? {
    if (type == null) return null
    return try {
        java.lang.Enum.valueOf(T::class.java, type)
    } catch (e: Exception) {
        null
    }
}