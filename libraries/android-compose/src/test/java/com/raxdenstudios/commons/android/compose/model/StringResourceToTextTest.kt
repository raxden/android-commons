package com.raxdenstudios.commons.android.compose.model

import android.content.res.Configuration
import android.content.res.Resources
import androidx.compose.runtime.AbstractApplier
import androidx.compose.runtime.BroadcastFrameClock
import androidx.compose.runtime.Composition
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Recomposer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.Snapshot
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalResources
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.Locale

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
internal class StringResourceToTextTest {

    private val clock = BroadcastFrameClock()
    private val testScope = CoroutineScope(Dispatchers.Unconfined)
    private lateinit var recomposer: Recomposer
    private lateinit var composition: Composition

    private var configuration: Configuration by mutableStateOf(Configuration().apply { setLocale(Locale.ENGLISH) })

    private fun language(): String = configuration.locales[0].language

    private fun varargs(
        args: List<Any?>,
    ): String = args.drop(1).flatMap { if (it is Array<*>) it.toList() else listOf(it) }.joinToString(",")

    private val resources: Resources = mockk {
        every { getString(ID) } returns "plain"
        every { getString(OTHER_ID) } returns "other plain"
        every { getString(ID, *anyVararg()) } answers {
            "hello ${language()} " + varargs(args)
        }
        every { getString(OTHER_ID, *anyVararg()) } answers {
            "bye ${language()} " + varargs(args)
        }
    }

    private var resource: StringResource by mutableStateOf(StringResource.Empty)
    private val results = mutableListOf<String>()

    @After
    fun tearDown() {
        if (::composition.isInitialized) composition.dispose()
        if (::recomposer.isInitialized) recomposer.cancel()
        testScope.cancel()
    }

    private fun render() {
        recomposer = Recomposer(testScope.coroutineContext + clock)
        testScope.launch(clock) { recomposer.runRecomposeAndApplyChanges() }
        composition = Composition(UnitApplier, recomposer)
        composition.setContent {
            val providedConfiguration = configuration
            val current = resource
            CompositionLocalProvider(
                LocalResources provides resources,
                LocalConfiguration provides providedConfiguration,
            ) {
                results += current.toText()
            }
        }
        idle()
    }

    @Test
    fun `Given ResourceId with params When toText Then it is formatted`() {
        resource = StringResource.ResourceId(ID, listOf("a", "b"))

        render()

        assertThat(results.last()).isEqualTo("hello en a,b")
    }

    @Test
    fun `Given ResourceId When params change Then it is reformatted`() {
        resource = StringResource.ResourceId(ID, listOf("a"))
        render()

        update { resource = StringResource.ResourceId(ID, listOf("z")) }

        assertThat(results.last()).isEqualTo("hello en z")
    }

    @Test
    fun `Given ResourceId When resourceId changes Then it is reformatted`() {
        resource = StringResource.ResourceId(ID, listOf("a"))
        render()

        update { resource = StringResource.ResourceId(OTHER_ID, listOf("a")) }

        assertThat(results.last()).isEqualTo("bye en a")
    }

    @Test
    fun `Given ResourceId When configuration changes Then it is reformatted`() {
        resource = StringResource.ResourceId(ID, listOf("a"))
        render()
        val before = results.size

        update { configuration = Configuration().apply { setLocale(Locale("es")) } }

        assertThat(results.size).isGreaterThan(before)
        assertThat(results.last()).isEqualTo("hello es a")
    }

    @Test
    fun `Given ResourceId with empty params When toText Then it is the plain resource`() {
        resource = StringResource.ResourceId(ID)

        render()

        assertThat(results.last()).isEqualTo("plain")
    }

    @Test
    fun `Given PlainText When toText Then it returns the value`() {
        resource = StringResource.PlainText("value")

        render()

        assertThat(results.last()).isEqualTo("value")
    }

    @Test
    fun `Given Empty When toText Then it returns empty string`() {
        resource = StringResource.Empty

        render()

        assertThat(results.last()).isEmpty()
    }

    private fun update(
        change: () -> Unit,
    ) {
        Snapshot.withMutableSnapshot(change)
        idle()
    }

    private fun idle() {
        Snapshot.sendApplyNotifications()
        clock.sendFrame(0L)
    }

    private companion object {
        const val ID = 1
        const val OTHER_ID = 2
    }
}

private object UnitApplier : AbstractApplier<Unit>(Unit) {
    override fun insertTopDown(
        index: Int,
        instance: Unit,
    ) = Unit
    override fun insertBottomUp(
        index: Int,
        instance: Unit,
    ) = Unit
    override fun remove(
        index: Int,
        count: Int,
    ) = Unit
    override fun move(
        from: Int,
        to: Int,
        count: Int,
    ) = Unit
    override fun onClear() = Unit
}
