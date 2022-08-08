package ru.vt.android

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import ru.vt.android.ui.CommonResourceManager
import ru.vt.core.common.ResourceManger
import ru.vt.core.dependency.*
import ru.vt.core.dependency.annotation.DependencyAccessor
import timber.log.Timber


@OptIn(DependencyAccessor::class)
internal class App : Application() {

    private companion object {
        const val APP_GLOBAL_TAG = "tag_vitals_tracker_"
    }

    override fun onCreate() {
        super.onCreate()

        initDependencies()

        initThreeTen()

        initTimber()
    }

    private fun initDependencies() {
        androidDeps = object : AndroidDependencies() {
            override val application: Application = this@App
            override val resourceManger: ResourceManger = CommonResourceManager(this@App.resources)
        }
        dataDeps = object : DataDependencies() {
            override val application: Application = this@App
        }
        sysServiceDeps = object : SystemServiceDependencies() {
            override val application: Application = this@App
        }
    }

    private fun initThreeTen() {
        AndroidThreeTen.init(this)
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(object :Timber.DebugTree() {
                override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                    super.log(priority, APP_GLOBAL_TAG + tag, message, t)
                }
            })
        }
    }
}
