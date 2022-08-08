package ru.vt.presentation.measurements.pressureheartrate.create

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import ru.vt.core.common.ResourceManger
import ru.vt.core.common.event.EventHandler
import ru.vt.core.common.extension.flowOnDefault
import ru.vt.core.common.extension.lazyNone
import ru.vt.core.dependency.androidDeps
import ru.vt.core.dependency.annotation.DependencyAccessor
import ru.vt.core.dependency.dataDeps
import ru.vt.core.navigation.contract.Navigator
import ru.vt.core.ui.feedback.FeedbackSnackbarEvent
import ru.vt.core.ui.viewmodel.BaseViewModel
import ru.vt.core.ui.viewmodel.ViewModelFactory
import ru.vt.domain.measurement.exceptions.EmptyValueException
import ru.vt.domain.measurement.exceptions.ValueRangeException
import ru.vt.domain.measurement.repository.BloodPressureRepository
import ru.vt.presentation.measurements.R
import timber.log.Timber
import java.util.*
import kotlin.random.Random
import ru.vt.domain.measurement.usecase.bloodpressure.AddBloodPressureUseCase as UseCase

@OptIn(DependencyAccessor::class)
internal class ScreenCreatePHRViewModel(
    savedStateHandle: SavedStateHandle,
    eventHandler: EventHandler?,
    resourceManger: ResourceManger,
    navigator: Navigator,
    private val repository: BloodPressureRepository,
) : BaseViewModel<ScreenCreatePHRViewModel.State, ScreenCreatePHRViewModel.Action>(
    savedStateHandle = savedStateHandle,
    eventHandler = eventHandler,
    resourceManger = resourceManger,
    navigator = navigator
) {

    override val initialState: State = State(date = LocalDateTime.now())

    private val useCase: UseCase by lazyNone {
        UseCase(repository = repository)
    }

    override suspend fun processAction(action: Action) {
        when (action) {
            is Action.Init -> render(state.copy(profileId = action.profileId))
            is Action.SetDate -> setDate(action.date)
            is Action.SetTime -> setTime(action.time)
            is Action.SetSystolic -> setSystolic(action.value)
            is Action.SetDiastolic -> setDiastolic(action.value)
            is Action.SetHeartRate -> setHeartRate(action.value)
            Action.Add -> add()
            Action.OnBackPressed -> navigator.onBackPressed()
        }
    }

    private fun setDate(date: Triple<Int, Int, Int>) {
        state.run {
            val newState = this.copy(
                date = LocalDateTime.of(
                    LocalDate.of(date.third, date.second, date.first),
                    this.date.toLocalTime()
                )
            )
            render(newState = newState)
        }
    }

    private fun setTime(time: Pair<Int, Int>) {
        state.run {
            val newState = this.copy(
                date = LocalDateTime.of(
                    this.date.toLocalDate(), LocalTime.of(time.first, time.second)
                )
            )
            render(newState = newState)
        }
    }

    private fun setSystolic(value: String?) {
        numberValueState(value = value) {
            render(state.copy(systolic = this))
        }
    }

    private fun setDiastolic(value: String?) {
        numberValueState(value = value) {
            render(state.copy(diastolic = this))
        }
    }

    private fun setHeartRate(value: String?) {
        numberValueState(value = value) {
            render(state.copy(heartRate = this))
        }
    }

    private fun add() {
        val params: UseCase.Params = UseCase.Params(
            profileId = state.profileId,
            timestamp = state.date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() + Random.nextLong(5000),
            systolic = state.systolic,
            diastolic = state.diastolic,
            heartRate = state.heartRate
        )
        useCase(params)
            .flowOnDefault()
            .handle(onSuccess = {
                if (it) {
                    viewModelScope.launch {
                        navigator.onBackPressed()
                    }
                }
            })
            .launchIn(scope = viewModelScope)
    }

    override fun handleError(t: Throwable): Boolean {
        return when (t) {
            is EmptyValueException -> {
                val name = resourceManger.getString(t.key)
                val msg = resourceManger.getString(R.string.add_measurement_value_empty_error, name)
                eventHandler?.handleEvent(FeedbackSnackbarEvent(msg))
                true
            }
            is ValueRangeException -> {
                val name = resourceManger.getString(t.key)
                val (from, to) = t.range.first.toString() to t.range.second.toString()
                val msg = resourceManger.getString(
                    R.string.add_measurement_value_range_error, name, from, to
                )
                eventHandler?.handleEvent(FeedbackSnackbarEvent(msg))
                true
            }
            else -> false
        }
    }

    private fun numberValueState(value: String?, block: Int?.() -> Unit) {
        try {
            value?.toInt()?.let(block)
        } catch (t: NumberFormatException) {
            block(null)
            Timber.e(t)
        }
    }

    sealed class Action {
        data class Init(val profileId: Long) : Action()
        data class SetDate(val date: Triple<Int, Int, Int>) : Action() // day, month, year
        data class SetTime(val time: Pair<Int, Int>) : Action() // hours, minutes
        data class SetSystolic(val value: String?) : Action()
        data class SetDiastolic(val value: String?) : Action()
        data class SetHeartRate(val value: String?) : Action()
        object Add : Action()
        object OnBackPressed : Action()
    }

    @Parcelize
    data class State(
        val profileId: Long = -1,
        val date: LocalDateTime,
        val systolic: Int? = null,
        val diastolic: Int? = null,
        val heartRate: Int? = null
    ) : Parcelable {

        fun getDateString(): String {
            val pattern = "dd MMMM YYYY"
            val dateFormatter: DateTimeFormatter =
                DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
            return date.format(dateFormatter)
        }

        fun getTimeString(): String {
            val pattern = "HH:mm"
            val dateFormatter: DateTimeFormatter =
                DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
            return date.format(dateFormatter)
        }
    }

    class Factory : ViewModelFactory<ScreenCreatePHRViewModel> {
        override fun create(handle: SavedStateHandle): ScreenCreatePHRViewModel =
            ScreenCreatePHRViewModel(
                savedStateHandle = handle,
                eventHandler = androidDeps.eventHandler,
                resourceManger = androidDeps.resourceManger,
                navigator = androidDeps.navigator,
                repository = dataDeps.bloodPressureRepository
            )
    }
}
