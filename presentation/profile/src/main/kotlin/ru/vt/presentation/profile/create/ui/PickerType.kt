package ru.vt.presentation.profile.create.ui

import androidx.annotation.IntDef

@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE)
@IntDef(PickerType.NONE, PickerType.DATE, PickerType.WEIGHT, PickerType.HEIGHT, PickerType.GENDER)
@Retention(AnnotationRetention.SOURCE)
internal annotation class PickerType {
    companion object {
        const val NONE: Int = 0
        const val DATE: Int = 1
        const val GENDER: Int = 2
        const val HEIGHT: Int = 3
        const val WEIGHT: Int = 4
    }
}
