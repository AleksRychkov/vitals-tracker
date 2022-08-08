package ru.vt.android.ui

import ru.vt.core.common.event.CommonEvent
import ru.vt.core.common.event.EventHandler
import ru.vt.core.ui.feedback.FeedbackEvent
import ru.vt.core.ui.feedback.FeedbackToastEvent
import ru.vt.core.ui.feedback.UiHost
import timber.log.Timber

internal class CommonEventHandler(
    private val uiHost: UiHost
) : EventHandler {

    override fun handleEvent(event: CommonEvent) {
        when (event) {
            is FeedbackEvent -> uiHost.handleFeedbackEvent(event = event)
        }
    }

    override fun handleError(t: Throwable) {
        Timber.e(t)
        uiHost.handleFeedbackEvent(FeedbackToastEvent(message = t.message ?: t.toString()))
    }
}
