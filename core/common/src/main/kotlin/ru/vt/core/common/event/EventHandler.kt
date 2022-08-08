package ru.vt.core.common.event

interface EventHandler {
    fun handleEvent(event: CommonEvent)
    fun handleError(t: Throwable)
}