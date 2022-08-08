package ru.vt.data.providers.sharedprefs

import android.content.SharedPreferences
import ru.vt.data.providers.ProfileProvider

internal class SharedPrefsProfileProvider(
    private val sharedPreferences: SharedPreferences
) : ProfileProvider {

    private companion object {
        const val DEFAULT_PROFILE_ID_KEY = "key.default_profile_id"
    }

    override var defaultProfileId: Long?
        get() {
            val id = sharedPreferences.getLong(DEFAULT_PROFILE_ID_KEY, -1L)
            return if (id != -1L) id else null
        }
        set(value) {
            value?.let {
                sharedPreferences.edit().putLong(DEFAULT_PROFILE_ID_KEY, value).commit()
            } ?: run { sharedPreferences.edit().remove(DEFAULT_PROFILE_ID_KEY).commit() }
        }
}