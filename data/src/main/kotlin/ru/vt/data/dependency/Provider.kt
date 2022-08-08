package ru.vt.data.dependency

import android.content.Context
import android.content.SharedPreferences
import com.ironz.binaryprefs.BinaryPreferencesBuilder
import ru.vt.data.datasource.DashboardDataSource
import ru.vt.data.datasource.measurements.BloodPressureDataSource
import ru.vt.data.datasource.measurements.HeadacheDataSource
import ru.vt.data.datasource.measurements.WeightDataSource
import ru.vt.data.providers.ProfileProvider
import ru.vt.data.providers.sharedprefs.SharedPrefsProfileProvider
import ru.vt.data.repositories.*
import ru.vt.data.source.db.*
import ru.vt.database.VitalsTrackerDB
import ru.vt.domain.dashboard.repository.DashboardRepository
import ru.vt.domain.measurement.repository.BloodPressureRepository
import ru.vt.domain.measurement.repository.HeadacheRepository
import ru.vt.domain.measurement.repository.WeightRepository
import ru.vt.domain.profile.repository.ProfileRepository

interface Provider {
    fun provideProfileRepository(): ProfileRepository
    fun provideDashboardRepository(): DashboardRepository
    fun provideBloodPressureRepository(): BloodPressureRepository
    fun provideHeadacheRepository(): HeadacheRepository
    fun provideWeightRepository(): WeightRepository
}

internal class ProviderImpl(
    private val context: Context
) : Provider {

    private val db: VitalsTrackerDB by lazy {
        VitalsTrackerDB.instantiate(context)
    }

    private val sharedPreferences: SharedPreferences by lazy {
        BinaryPreferencesBuilder(context)
            .name("vitals-tracker")
            .build()
    }

    private val profileProvider: ProfileProvider by lazy {
        SharedPrefsProfileProvider(sharedPreferences)
    }

    private val profileDS: DBProfileDataSource by lazy { DBProfileDataSource(db) }
    private val dashboardDS: DashboardDataSource by lazy { DBDashboardDataSource(db) }
    private val bloodPressureDataSource: BloodPressureDataSource by lazy { DbBloodPressureDataSource(db) }
    private val headacheDataSource: HeadacheDataSource by lazy { DbHeadacheDataSource(db) }
    private val weightDataSource: WeightDataSource by lazy { DbWeightDataSource(db) }

    override fun provideProfileRepository(): ProfileRepository =
        ProfileRepositoryImpl(profileDS, profileProvider)

    override fun provideBloodPressureRepository(): BloodPressureRepository =
        BloodPressureRepositoryImpl(bloodPressureDataSource)

    override fun provideDashboardRepository(): DashboardRepository =
        DashboardRepositoryImpl(dashboardDS)

    override fun provideHeadacheRepository(): HeadacheRepository =
        HeadacheRepositoryImpl(headacheDataSource)

    override fun provideWeightRepository(): WeightRepository =
        WeightRepositoryImpl(weightDataSource)
}

fun provide(context: Context): Provider = ProviderImpl(context)
