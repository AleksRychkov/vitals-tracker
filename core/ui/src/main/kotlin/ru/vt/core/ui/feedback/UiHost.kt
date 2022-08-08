package ru.vt.core.ui.feedback

interface UiHost {
    fun handleFeedbackEvent(event: FeedbackEvent)

    fun setLoading(loading: Boolean)
}