package ru.vt.core.dependency

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import ru.vt.core.common.LoaderHandler
import ru.vt.core.common.ResourceManger
import ru.vt.core.common.event.EventHandler
import ru.vt.core.dependency.annotation.DependencyAccessor
import ru.vt.core.navigation.contract.Navigator

@DependencyAccessor
public lateinit var androidDeps: AndroidDependencies

@OptIn(DependencyAccessor::class)
public val LifecycleOwner.androidDeps: AndroidDependencies
    get() = ru.vt.core.dependency.androidDeps

@OptIn(DependencyAccessor::class)
public abstract class AndroidDependencies {
    public abstract val application: Application

    public abstract val resourceManger: ResourceManger

    public var eventHandler: EventHandler? = null

    public var loaderHandler: LoaderHandler? = null

    public lateinit var navigator: Navigator
}