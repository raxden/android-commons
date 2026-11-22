package com.raxdenstudios.commons.android.test

import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.test.runner.AndroidJUnitRunner

/**
 * Base instrumentation runner that extends `AndroidJUnitRunner`. Subclass it to swap the [Application] used
 * by the tests.
 *
 * ## Setup
 *
 * Declare the subclass as the test runner in the app module's Gradle file, for example
 * `testInstrumentationRunner = "com.example.TestRunner"`.
 *
 * ## Usage
 *
 * ```kotlin
 * class TestRunner : AppTestRunner() {
 *     override fun newApplication(cl: ClassLoader?, className: String?, context: Context?): Application =
 *         super.newApplication(cl, TestApplication::class.java.name, context)
 * }
 * ```
 */
open class AppTestRunner : AndroidJUnitRunner() {

    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?,
    ): Application = super.newApplication(cl, className, context)

    override fun onCreate(
        arguments: Bundle,
    ) {
        super.onCreate(arguments)
    }
}
