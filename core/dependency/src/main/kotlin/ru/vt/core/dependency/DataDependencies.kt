package ru.vt.core.dependency

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import ru.vt.core.dependency.annotation.DependencyAccessor
import ru.vt.data.dependency.Provider
import ru.vt.data.dependency.provide
import ru.vt.domain.dashboard.repository.DashboardRepository
import ru.vt.domain.measurement.repository.BloodPressureRepository
import ru.vt.domain.measurement.repository.HeadacheRepository
import ru.vt.domain.measurement.repository.WeightRepository
import ru.vt.domain.profile.repository.ProfileRepository

@DependencyAccessor
public lateinit var dataDeps: DataDependencies

@OptIn(DependencyAccessor::class)
public val LifecycleOwner.dataDeps: DataDependencies
    get() = ru.vt.core.dependency.dataDeps

@OptIn(DependencyAccessor::class)
public abstract class DataDependencies {

    public abstract val application: Application

    private val dataProvider: Provider by lazy {
        provide(application)
    }

    public val bloodPressureRepository: BloodPressureRepository by lazy {
        dataProvider.provideBloodPressureRepository()
    }

    public val profileRepository: ProfileRepository by lazy {
        dataProvider.provideProfileRepository()
    }

    public val dashboardRepository: DashboardRepository by lazy {
        dataProvider.provideDashboardRepository()
    }

    public val headacheRepository: HeadacheRepository by lazy {
        dataProvider.provideHeadacheRepository()
    }

    public val weightRepository: WeightRepository by lazy {
        dataProvider.provideWeightRepository()
    }
}
