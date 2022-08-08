package ru.vt.presentation.measurements.headaches.summary

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize
import ru.vt.domain.measurement.entity.HeadacheEntity
import ru.vt.presentation.measurements.common.entity.AnchorDate
import ru.vt.presentation.measurements.common.entity.GraphSection
import ru.vt.presentation.measurements.common.entity.Period

@Parcelize
internal data class StateHeadachesSummary(
    val profileId: Long = -1,
    val period: Period = Period.Day,
    val anchorDate: AnchorDate,
    val graphData: List<GraphSection>? = null,
    val data: List<HeadacheEntity>? = null,
    val canLoadMore: Boolean = false
) : Parcelable {
    private companion object : Parceler<StateHeadachesSummary> {
        override fun create(parcel: Parcel): StateHeadachesSummary {
            return StateHeadachesSummary(
                profileId = parcel.readLong(),
                anchorDate = parcel.readParcelable(AnchorDate::class.java.classLoader)!!,
                period = parcel.readParcelable(Period::class.java.classLoader)!!,
                canLoadMore = parcel.readByte() == 1.toByte()
            )
        }

        override fun StateHeadachesSummary.write(parcel: Parcel, flags: Int) {
            parcel.writeLong(profileId)
            parcel.writeParcelable(anchorDate, flags)
            parcel.writeParcelable(period, flags)
            parcel.writeByte(if (canLoadMore) 1 else 0)
        }
    }
}
