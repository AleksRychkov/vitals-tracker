package ru.vt.core.dependency

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import ru.vt.core.dependency.annotation.DependencyAccessor
import ru.vt.core.sysservice.VibratorService
import ru.vt.core.sysservice.dependency.provideVibratorService

@DependencyAccessor
public lateinit var sysServiceDeps: SystemServiceDependencies

@OptIn(DependencyAccessor::class)
public val LifecycleOwner.sysServiceDeps: SystemServiceDependencies
    get() = ru.vt.core.dependency.sysServiceDeps

@OptIn(DependencyAccessor::class)
public abstract class SystemServiceDependencies {
    public abstract val application: Application

    public val vibratorService: VibratorService by lazy {
        provideVibratorService(application)
    }
}