package ru.vt.presentation.measurements.weight.summary

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize
import ru.vt.domain.measurement.entity.WeightEntity
import ru.vt.presentation.measurements.common.entity.AnchorDate
import ru.vt.presentation.measurements.common.entity.GraphSection
import ru.vt.presentation.measurements.common.entity.Period

@Parcelize
internal data class StateWeightSummary(
    val profileId: Long = -1,
    val period: Period = Period.Day,
    val anchorDate: AnchorDate,
    val graphData: List<GraphSection>? = null,
    val data: List<WeightEntity>? = null,
    val canLoadMore: Boolean = false
) : Parcelable {
    private companion object : Parceler<StateWeightSummary> {
        override fun create(parcel: Parcel): StateWeightSummary {
            return StateWeightSummary(
                profileId = parcel.readLong(),
                anchorDate = parcel.readParcelable(AnchorDate::class.java.classLoader)!!,
                period = parcel.readParcelable(Period::class.java.classLoader)!!,
                canLoadMore = parcel.readByte() == 1.toByte()
            )
        }

        override fun StateWeightSummary.write(parcel: Parcel, flags: Int) {
            parcel.writeLong(profileId)
            parcel.writeParcelable(anchorDate, flags)
            parcel.writeParcelable(period, flags)
            parcel.writeByte(if (canLoadMore) 1 else 0)
        }
    }
}
