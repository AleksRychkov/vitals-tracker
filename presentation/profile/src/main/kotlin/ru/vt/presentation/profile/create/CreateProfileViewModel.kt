package ru.vt.presentation.profile.create

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import ru.vt.core.common.ResourceManger
import ru.vt.core.common.event.EventHandler
import ru.vt.core.common.extension.lazyNone
import ru.vt.core.dependency.androidDeps
import ru.vt.core.dependency.annotation.DependencyAccessor
import ru.vt.core.dependency.dataDeps
import ru.vt.core.navigation.contract.Navigator
import ru.vt.core.navigation.contract.ProfileCreateToDashboard
import ru.vt.core.ui.feedback.FeedbackSnackbarEvent
import ru.vt.core.ui.viewmodel.BaseViewModel
import ru.vt.core.ui.viewmodel.ViewModelFactory
import ru.vt.domain.profile.exceptions.EmptyNameError
import ru.vt.domain.profile.exceptions.UniqueNameError
import ru.vt.domain.profile.repository.ProfileRepository
import ru.vt.domain.profile.usecase.SaveProfileUseCase
import ru.vt.presentation.profile.R

@OptIn(DependencyAccessor::class)
internal class CreateProfileViewModel(
    savedStateHandle: SavedStateHandle,
    eventHandler: EventHandler?,
    resourceManger: ResourceManger,
    navigator: Navigator,
    private val profileRepository: ProfileRepository
) : BaseViewModel<StateCreateProfile, ActionCreateProfile>(
    savedStateHandle = savedStateHandle,
    eventHandler = eventHandler,
    resourceManger = resourceManger,
    navigator = navigator
) {

    private val saveProfileUseCase: SaveProfileUseCase by lazyNone {
        SaveProfileUseCase(profileRepository)
    }

    override val initialState: StateCreateProfile = StateCreateProfile()

    override suspend fun processAction(action: ActionCreateProfile) {
        when (action) {
            ActionCreateProfile.CreateProfile -> createProfile()
            is ActionCreateProfile.UserInput -> updateState(action)
        }
    }

    override fun handleError(t: Throwable): Boolean {
        return when (t) {
            is UniqueNameError -> {
                eventHandler?.handleEvent(FeedbackSnackbarEvent(resourceManger.getString(R.string.create_profile_field_name_already_exists)))
                true
            }
            is EmptyNameError -> {
                eventHandler?.handleEvent(FeedbackSnackbarEvent(resourceManger.getString(R.string.create_profile_field_name_is_required)))
                true
            }
            else -> false
        }
    }

    private fun createProfile() {
        saveProfileUseCase.invoke(
            SaveProfileUseCase.Params(
                name = state.name,
                birthdate = state.dateOfBirth?.toEntity(),
                gender = state.gender,
                heightCm = state.heightCm,
                weightG = state.weightG
            )
        )
            .handle(onSuccess = {
                if (state.id == null) {
                    viewModelScope.launch {
                        navigator.navigate(ProfileCreateToDashboard(it))
                    }
                } else {
                    //todo: figure out navigation in case of edit profile
                }
                render(state.copy(id = it))
            })
            .launchIn(scope = viewModelScope)
    }

    private fun updateState(action: ActionCreateProfile.UserInput) {
        render(
            state.copy(
                name = action.name,
                dateOfBirth = action.dateOfBirth,
                gender = action.gender,
                heightCm = action.heightCm,
                weightG = action.weightG
            )
        )
    }

    class Factory : ViewModelFactory<CreateProfileViewModel> {
        override fun create(handle: SavedStateHandle): CreateProfileViewModel =
            CreateProfileViewModel(
                savedStateHandle = handle,
                eventHandler = androidDeps.eventHandler,
                resourceManger = androidDeps.resourceManger,
                navigator = androidDeps.navigator,
                profileRepository = dataDeps.profileRepository
            )
    }
}
