package ru.vt.presentation.measurements.pressureheartrate.summary

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize
import ru.vt.domain.measurement.entity.BloodPressureEntity
import ru.vt.presentation.measurements.common.entity.AnchorDate
import ru.vt.presentation.measurements.common.entity.GraphSection
import ru.vt.presentation.measurements.common.entity.Period

@Parcelize
internal data class StatePHR(
    val profileId: Long = -1L,
    val anchorDate: AnchorDate,
    val period: Period = Period.Day,
    val graphData: List<GraphSection>? = null,
    val data: List<BloodPressureEntity>? = null,
    val canLoadMore: Boolean = false
) : Parcelable {
    private companion object : Parceler<StatePHR> {
        override fun StatePHR.write(parcel: Parcel, flags: Int) {
            parcel.writeLong(profileId)
            parcel.writeParcelable(anchorDate, flags)
            parcel.writeParcelable(period, flags)
            parcel.writeByte(if (canLoadMore) 1 else 0)
        }

        override fun create(parcel: Parcel): StatePHR {
            return StatePHR(
                profileId = parcel.readLong(),
                anchorDate = parcel.readParcelable(AnchorDate::class.java.classLoader)!!,
                period = parcel.readParcelable(Period::class.java.classLoader)!!,
                canLoadMore = parcel.readByte() == 1.toByte()
            )
        }
    }
}
