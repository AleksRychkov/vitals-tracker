package ru.vt.core.dependency.annotation

/*
Copied from https://github.com/ensody/ReactiveState-Kotlin/blob/main/reactivestate/src/commonMain/kotlin/com/ensody/reactivestate/Annotations.kt
 */

/** Marks dependency injection system accessors, so direct access must be explicitly opted in. */
@MustBeDocumented
@RequiresOptIn(
    message = "Direct access to the DI causes tight coupling. If possible, use constructor injection or parameters.",
    level = RequiresOptIn.Level.ERROR,
)
@Retention(value = AnnotationRetention.BINARY)
@Target(AnnotationTarget.PROPERTY)
public annotation class DependencyAccessor