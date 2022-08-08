package ru.vt.core.ui.viewmodel

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.lifecycle.SavedStateHandle
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import ru.vt.core.common.ResourceManger
import ru.vt.core.common.event.EventHandler
import ru.vt.core.navigation.contract.Navigator
import ru.vt.core.test.StubMainThreadExtension

@ExtendWith(MockKExtension::class, StubMainThreadExtension::class)
abstract class AbstractViewModelTest<T : BaseViewModel<*, *>> {
    @MockK(relaxed = true)
    protected lateinit var savedStateHandle: SavedStateHandle

    @MockK(relaxed = true)
    protected lateinit var resourceManger: ResourceManger

    @MockK(relaxed = true)
    protected lateinit var eventHandler: EventHandler

    @MockK(relaxed = true)
    protected lateinit var navigator: Navigator

    @SpyK
    protected var testErrorHook: TestErrorHook = { _, _ -> }

    @InjectMockKs
    protected lateinit var vm: T

    @BeforeEach
    @CallSuper
    protected open fun init() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        vm.testErrorHook = testErrorHook
        coEvery { savedStateHandle.get<Bundle>(any()) } returns Bundle()
    }
}
