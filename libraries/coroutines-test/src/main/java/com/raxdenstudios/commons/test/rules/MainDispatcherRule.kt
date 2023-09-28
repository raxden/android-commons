package com.raxdenstudios.commons.test.rules

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * Reusable JUnit4 TestRule to override the Main dispatcher. This is helpful when you want to execute
 * a test in situations where the platform Main dispatcher is not available, or you wish to
 * replace Dispatchers.Main with a testing dispatcher for example in viewModels
 * More info -> https://developer.android.com/kotlin/coroutines/test
 */
@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule constructor(
    val testDispatcher: TestDispatcher = UnconfinedTestDispatcher(),
) : TestWatcher() {

    override fun starting(description: Description) {
        super.starting(description)
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        super.finished(description)
        Dispatchers.resetMain()
    }
}
