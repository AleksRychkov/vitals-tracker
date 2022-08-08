package ru.vt.core.common

import androidx.annotation.StringRes

interface ResourceManger {
    fun getString(@StringRes id: Int): String
    fun getString(@StringRes id: Int, vararg p: String): String
    fun getString(key: String): String
}
