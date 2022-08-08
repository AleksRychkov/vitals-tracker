package ru.vt.presentation.measurements.headaches.create

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
import ru.vt.domain.measurement.entity.HeadacheArea
import ru.vt.domain.measurement.exceptions.EmptyValueException
import ru.vt.domain.measurement.repository.HeadacheRepository
import ru.vt.domain.measurement.usecase.headache.AddHeadacheUseCase
import ru.vt.presentation.measurements.R
import java.util.*
import kotlin.random.Random

@OptIn(DependencyAccessor::class)
internal class ScreenCreateHeadachesViewModel(
    savedStateHandle: SavedStateHandle,
    eventHandler: EventHandler?,
    resourceManger: ResourceManger,
    navigator: Navigator,
    private val repository: HeadacheRepository
) : BaseViewModel<ScreenCreateHeadachesViewModel.State, ScreenCreateHeadachesViewModel.Action>(
    savedStateHandle = savedStateHandle,
    eventHandler = eventHandler,
    resourceManger = resourceManger,
    navigator = navigator
) {

    private val useCase: AddHeadacheUseCase by lazyNone {
        AddHeadacheUseCase(repository = repository)
    }

    override val initialState: State = State(date = LocalDateTime.now())

    override suspend fun processAction(action: Action) {
        when (action) {
            is Action.Init -> render(state.copy(profileId = action.profileId))
            is Action.SetDate -> setDate(action.date)
            is Action.SetTime -> setTime(action.time)
            is Action.SetHeadacheIntensity -> setHeadacheIntensity(action.value)
            is Action.SetHeadacheArea -> setHeadacheArea(action.value)
            is Action.SetHeadacheDescription -> setHeadacheDescription(action.value)
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

    private fun setHeadacheIntensity(value: Int?) {
        state.run {
            val newState = this.copy(
                headacheIntensity = value
            )
            render(newState = newState)
        }
    }

    private fun setHeadacheArea(value: HeadacheArea) {
        state.run {
            val newState = this.copy(
                headacheArea = value
            )
            render(newState = newState)
        }
    }

    private fun setHeadacheDescription(value: String?) {
        state.run {
            val newState = this.copy(
                description = value
            )
            render(newState = newState)
        }
    }

    private fun add() {
        val params: AddHeadacheUseCase.Params = AddHeadacheUseCase.Params(
            profileId = state.profileId,
            timestamp = state.date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() + Random.nextLong(5000),
            intensity = state.headacheIntensity,
            area = state.headacheArea,
            description = state.description
        )
        useCase(params = params)
            .flowOnDefault()
            .handle(onSuccess = {
                if (it) {
                    viewModelScope.launch {
                        navigator.onBackPressed()
                    }
                }
            })
            .launchIn(viewModelScope)
    }

    override fun handleError(t: Throwable): Boolean {
        if (t is EmptyValueException) {
            val name = resourceManger.getString(t.key)
            val msg = resourceManger.getString(R.string.add_measurement_value_empty_error, name)
            eventHandler?.handleEvent(FeedbackSnackbarEvent(msg))
            return true
        }
        return false
    }

    sealed class Action {
        data class Init(val profileId: Long) : Action()
        data class SetDate(val date: Triple<Int, Int, Int>) : Action() // day, month, year
        data class SetTime(val time: Pair<Int, Int>) : Action() // hours, minutes
        data class SetHeadacheIntensity(val value: Int?) : Action()
        data class SetHeadacheArea(val value: HeadacheArea) : Action()
        data class SetHeadacheDescription(val value: String?) : Action()

        object Add : Action()
        object OnBackPressed : Action()
    }

    @Parcelize
    data class State(
        val profileId: Long = -1,
        val date: LocalDateTime,
        val headacheIntensity: Int? = null,
        val headacheArea: HeadacheArea = HeadacheArea.UNDEFINED,
        val description: String? = null
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

    class Factory : ViewModelFactory<ScreenCreateHeadachesViewModel> {
        override fun create(handle: SavedStateHandle): ScreenCreateHeadachesViewModel =
            ScreenCreateHeadachesViewModel(
                savedStateHandle = handle,
                eventHandler = androidDeps.eventHandler,
                resourceManger = androidDeps.resourceManger,
                navigator = androidDeps.navigator,
                repository = dataDeps.headacheRepository
            )
    }
}
