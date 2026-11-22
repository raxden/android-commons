package com.raxdenstudios.commons.android.test.rules

import androidx.test.platform.app.InstrumentationRegistry
import io.appflate.restmock.RESTMockOptions
import io.appflate.restmock.RESTMockServer
import io.appflate.restmock.RESTMockServerStarter
import io.appflate.restmock.android.AndroidAssetsFileParser
import io.appflate.restmock.android.AndroidLogger
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * JUnit rule that starts the RESTMock server before each test and resets it after.
 *
 * The server reads its mock responses from the test assets and serves HTTPS unless [useHttps] is `false`.
 * This rule is the only place that starts RESTMock: do not call `RESTMockServerStarter` in the test or in
 * `@Before`, or the server is started twice.
 *
 * ## Usage
 *
 * ```kotlin
 * @get:Rule
 * val restMockServerRule = RESTMockServerRule()
 * ```
 */
@Deprecated(
    message = "Will be removed in the next major version. " +
        "Keep a project-owned rule that starts RESTMockServer instead.",
    level = DeprecationLevel.WARNING,
)
class RESTMockServerRule(
    private val useHttps: Boolean = true,
) : TestWatcher() {

    override fun starting(
        description: Description?,
    ) {
        super.starting(description)
        RESTMockServerStarter.startSync(
            AndroidAssetsFileParser(InstrumentationRegistry.getInstrumentation().context),
            AndroidLogger(),
            RESTMockOptions.Builder().useHttps(useHttps).build(),
        )
    }

    override fun finished(
        description: Description?,
    ) {
        super.finished(description)
        RESTMockServer.reset()
    }
}
