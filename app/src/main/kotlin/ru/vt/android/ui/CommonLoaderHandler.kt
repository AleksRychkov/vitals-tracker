package ru.vt.android.ui

import ru.vt.core.common.LoaderHandler
import ru.vt.core.ui.feedback.UiHost

internal class CommonLoaderHandler(
    private val uiHost: UiHost
) : LoaderHandler {
    override fun setLoading(loading: Boolean) {
        uiHost.setLoading(loading = loading)
    }
}
