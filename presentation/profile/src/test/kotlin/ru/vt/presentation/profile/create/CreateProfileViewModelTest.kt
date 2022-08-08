package ru.vt.presentation.profile.create

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import ru.vt.core.common.ResourceManger
import ru.vt.core.common.event.EventHandler
import ru.vt.core.navigation.contract.Navigator
import ru.vt.core.navigation.contract.ProfileCreateToDashboard
import ru.vt.core.test.StubMainThreadExtension
import ru.vt.core.ui.feedback.FeedbackSnackbarEvent
import ru.vt.core.ui.viewmodel.TestErrorHook
import ru.vt.domain.profile.exceptions.EmptyNameError
import ru.vt.domain.profile.exceptions.UniqueNameError
import ru.vt.domain.profile.repository.ProfileRepository

@ExtendWith(MockKExtension::class, StubMainThreadExtension::class)
internal class CreateProfileViewModelTest {

    @MockK(relaxed = true)
    private lateinit var savedStateHandle: SavedStateHandle

    @MockK(relaxed = true)
    private lateinit var resourceManger: ResourceManger

    @MockK(relaxed = true)
    private lateinit var eventHandler: EventHandler

    @MockK(relaxed = true)
    private lateinit var navigator: Navigator

    @MockK
    private lateinit var repository: ProfileRepository

    @SpyK
    private var testErrorHook: TestErrorHook = { _, _ -> }

    @InjectMockKs
    private lateinit var vm: CreateProfileViewModel

    @BeforeEach
    fun init() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        vm.testErrorHook = testErrorHook
        coEvery { savedStateHandle.get<Bundle>(any()) } returns Bundle()
    }

    @Test
    fun `should send FeedbackSnackbarEvent when failed to create profile due to EmptyNameError`(): Unit =
        runBlocking {
            vm.submitAction(ActionCreateProfile.UserInput())
            vm.submitAction(ActionCreateProfile.CreateProfile)

            delay(100)
            coVerify(exactly = 1) { eventHandler.handleEvent(any<FeedbackSnackbarEvent>()) }
            coVerify(exactly = 1) { testErrorHook.invoke(EmptyNameError, true) }
        }

    @Test
    fun `should send FeedbackSnackbarEvent when failed to create profile due to UniqueNameError`(): Unit =
        runBlocking {
            val name = "Name"
            coEvery { repository.isNameAvailable(name) } returns false

            vm.submitAction(ActionCreateProfile.UserInput(name = name))
            vm.submitAction(ActionCreateProfile.CreateProfile)

            delay(100)
            coVerify(exactly = 1) { eventHandler.handleEvent(any<FeedbackSnackbarEvent>()) }
            coVerify(exactly = 1) { testErrorHook.invoke(UniqueNameError, true) }
        }

    @Test
    fun `should navigate to dashboard after profile is successfully created`(): Unit = runBlocking {
        val profileId = 123L
        coEvery { repository.isNameAvailable(any()) } returns true
        coEvery { repository.saveProfile(any()) } returns profileId

        vm.submitAction(ActionCreateProfile.UserInput(name = "name"))
        vm.submitAction(ActionCreateProfile.CreateProfile)

        vm.stateFlow.test {
            var state: StateCreateProfile?
            do {
                state = awaitItem()
            } while (state?.id == null)
            assertTrue(state.id == profileId)
        }

        coVerify(exactly = 0) { eventHandler.handleEvent(any<FeedbackSnackbarEvent>()) }
        coVerify(exactly = 0) { testErrorHook.invoke(UniqueNameError, true) }
        coVerify(exactly = 0) { testErrorHook.invoke(EmptyNameError, true) }

        coVerify { navigator.navigate(any<ProfileCreateToDashboard>()) }
    }
}
