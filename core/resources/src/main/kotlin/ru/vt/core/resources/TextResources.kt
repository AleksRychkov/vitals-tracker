package ru.vt.core.resources

import android.content.res.Resources
import androidx.annotation.StringRes
import ru.vt.domain.common.AppGender
import ru.vt.domain.common.AppUnits
import ru.vt.domain.common.Measurement
import ru.vt.domain.common.MeasurementParams

// todo: re-think about this class
object TextResources {

    private const val MEASUREMENT_KEY = "measurement_"

    private val stringResMap: Map<String, Int> = mapOf(
        "january" to R.string.JANUARY,
        "february" to R.string.FEBRUARY,
        "march" to R.string.MARCH,
        "april" to R.string.APRIL,
        "may" to R.string.MAY,
        "june" to R.string.JUNE,
        "july" to R.string.JULY,
        "august" to R.string.AUGUST,
        "september" to R.string.SEPTEMBER,
        "october" to R.string.OCTOBER,
        "november" to R.string.NOVEMBER,
        "december" to R.string.DECEMBER,

        AppGender.MALE.key to R.string.MALE,
        AppGender.FEMALE.key to R.string.FEMALE,
        AppGender.OTHER.key to R.string.OTHER,

        AppUnits.KG.key to R.string.unit_kg,
        AppUnits.GRAMS.key to R.string.unit_grams,
        AppUnits.CM.key to R.string.unit_cm,
        AppUnits.BLOOD_PRESSURE.key to R.string.unit_blood_pressure,
        AppUnits.HEART_RATE.key to R.string.unit_heart_rate,

        MeasurementParams.SYSTOLIC.key to R.string.systolic,
        MeasurementParams.DIASTOLIC.key to R.string.diastolic,
        MeasurementParams.HEART_RATE.key to R.string.heart_rate,

        MeasurementParams.HEADACHE_INTENSITY.key to R.string.headache_intensity,
        MeasurementParams.HEADACHE_ALL.key to R.string.headache_area_all,
        MeasurementParams.HEADACHE_LEFT.key to R.string.headache_area_left,
        MeasurementParams.HEADACHE_RIGHT.key to R.string.headache_area_right,
        MeasurementParams.HEADACHE_FOREHEAD.key to R.string.headache_area_forehead,
        MeasurementParams.HEADACHE_BACK_HEAD.key to R.string.headache_area_back_head,

        MeasurementParams.WEIGHT.key to R.string.measurement_weight,

        "$MEASUREMENT_KEY${Measurement.HEADACHE}" to R.string.measurement_headache,
        "$MEASUREMENT_KEY${Measurement.WEIGHT}" to R.string.measurement_weight,
        "$MEASUREMENT_KEY${Measurement.PRESSURE_AND_HEART_RATE}" to R.string.measurement_blood_pressure_and_heart_rate,
    )

    fun getString(key: String, resources: Resources): String {
        return stringResMap[key.lowercase()]?.let {
            resources.getString(it)
        } ?: key
    }

    fun getString(@StringRes id: Int, resources: Resources): String =
        resources.getString(id)

    fun getString(@StringRes id: Int, resources: Resources, vararg p: String): String =
        resources.getString(id, *p)

    fun getNameOfMeasurement(@Measurement type: Int, resources: Resources): String {
        val key = "$MEASUREMENT_KEY$type"
        return stringResMap[key.lowercase()]?.let {
            resources.getString(it)
        } ?: key
    }
}