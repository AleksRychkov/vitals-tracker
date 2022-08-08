package ru.vt.core.ui.feedback

import android.widget.Toast

class FeedbackToastEvent(val message: String, val length: Int = Toast.LENGTH_SHORT) : FeedbackEvent