package ru.vt.core.ui.feedback

import androidx.compose.material.SnackbarDuration

data class FeedbackSnackbarEvent(
    val message: String,
    val duration: SnackbarDuration = SnackbarDuration.Short
) : FeedbackEvent