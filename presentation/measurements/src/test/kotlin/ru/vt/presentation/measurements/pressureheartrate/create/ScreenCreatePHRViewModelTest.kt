package ru.vt.presentation.measurements.pressureheartrate.create

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
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import ru.vt.core.common.ResourceManger
import ru.vt.core.common.event.EventHandler
import ru.vt.core.navigation.contract.Navigator
import ru.vt.core.test.StubMainThreadExtension
import ru.vt.core.ui.feedback.FeedbackSnackbarEvent
import ru.vt.core.ui.viewmodel.TestErrorHook
import ru.vt.domain.measurement.repository.BloodPressureRepository
import ru.vt.presentation.measurements.pressureheartrate.create.ScreenCreatePHRViewModel.Action
import ru.vt.presentation.measurements.pressureheartrate.create.ScreenCreatePHRViewModel.State
import ru.vt.presentation.measurements.pressureheartrate.create.ScreenCreatePHRViewModel as VM

@ExtendWith(MockKExtension::class, StubMainThreadExtension::class)
class ScreenCreatePHRViewModelTest {

    @MockK(relaxed = true)
    private lateinit var savedStateHandle: SavedStateHandle

    @MockK(relaxed = true)
    private lateinit var resourceManger: ResourceManger

    @MockK(relaxed = true)
    private lateinit var eventHandler: EventHandler

    @MockK(relaxed = true)
    private lateinit var navigator: Navigator

    @MockK(relaxed = true)
    private lateinit var repository: BloodPressureRepository

    @SpyK
    private var testErrorHook: TestErrorHook = { _, _ -> }

    @InjectMockKs
    private lateinit var vm: VM

    private val profileId: Long = 123L

    @BeforeEach
    fun init() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        vm.testErrorHook = testErrorHook
        coEvery { savedStateHandle.get<Bundle>(any()) } returns Bundle()
    }

    @Nested
    inner class AddActionCheck {

        @Test
        fun `should send event when failed to add`() = runBlocking {
            vm.submitAction(Action.Init(profileId = profileId))
            vm.submitAction(Action.Add)

            vm.stateFlow.test {
                var state: State?
                do {
                    state = awaitItem()
                } while (state == null)
                delay(100)
            }
            coVerify(exactly = 1) { eventHandler.handleEvent(FeedbackSnackbarEvent("")) }
        }

        @Test
        fun `should set isSuccess when add called`() = runBlocking {
            vm.submitAction(Action.Init(profileId = profileId))

            vm.submitAction(Action.SetSystolic("120"))
            vm.submitAction(Action.SetDiastolic("90"))

            vm.submitAction(Action.Add)

            coVerify(exactly = 0) { eventHandler.handleEvent(any()) }
            coVerify(exactly = 1) { navigator.onBackPressed() }
        }
    }
}
