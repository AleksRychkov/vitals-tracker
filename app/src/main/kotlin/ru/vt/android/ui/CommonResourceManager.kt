package ru.vt.android.ui

import android.content.res.Resources
import ru.vt.core.common.ResourceManger
import ru.vt.core.resources.TextResources

internal class CommonResourceManager(private val resources: Resources) : ResourceManger {
    override fun getString(id: Int): String = TextResources.getString(id, resources)
    override fun getString(key: String): String = TextResources.getString(key, resources)
    override fun getString(id: Int, vararg p: String): String = TextResources.getString(id, resources, *p)
}
