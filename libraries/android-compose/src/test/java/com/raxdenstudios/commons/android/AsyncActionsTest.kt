package com.raxdenstudios.commons.android

import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.CoroutineScope
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
internal class AsyncActionsTest {

    private val activity = Robolectric.buildActivity(ComponentActivity::class.java).setup().get()

    private val calls = mutableListOf<String>()
    private var tick by mutableStateOf(0)
    private var runBlock: suspend CoroutineScope.() -> Unit = { calls += "first" }
    private var oneArgBlock: suspend CoroutineScope.(String) -> Unit = { calls += "first:$it" }
    private var twoArgBlock: suspend CoroutineScope.(String, Int) -> Unit = { a, b -> calls += "first:$a:$b" }
    private var blockVersion by mutableStateOf(0)

    @Test
    fun `Given recompositions When runAsync Then the same lambda instance is returned`() {
        val instances = mutableListOf<() -> Unit>()
        activity.setContent {
            tick.let { }
            instances += runAsync { runBlock() }
        }
        idle()

        tick++
        idle()

        assertThat(instances.size).isAtLeast(2)
        instances.forEach { assertThat(it).isSameInstanceAs(instances.first()) }
    }

    @Test
    fun `Given a new block When runAsync lambda is invoked Then the new block runs`() {
        var action: (() -> Unit)? = null
        activity.setContent {
            val version = blockVersion
            action = runAsync { calls += "block$version" }
        }
        idle()

        blockVersion = 1
        idle()
        checkNotNull(action).invoke()
        idle()

        assertThat(calls).containsExactly("block1")
    }

    @Test
    fun `Given recompositions When consumeAsync with one arg Then the same lambda instance is returned`() {
        val instances = mutableListOf<(String) -> Unit>()
        activity.setContent {
            tick.let { }
            instances += consumeAsync<String> { calls += it }
        }
        idle()

        tick++
        idle()

        assertThat(instances.size).isAtLeast(2)
        instances.forEach { assertThat(it).isSameInstanceAs(instances.first()) }
    }

    @Test
    fun `Given a new block When consumeAsync with one arg is invoked Then the new block receives the value`() {
        var action: ((String) -> Unit)? = null
        activity.setContent {
            val version = blockVersion
            action = consumeAsync<String> { calls += "block$version:$it" }
        }
        idle()

        blockVersion = 1
        idle()
        checkNotNull(action).invoke("x")
        idle()

        assertThat(calls).containsExactly("block1:x")
    }

    @Test
    fun `Given recompositions When consumeAsync with two args Then the same lambda instance is returned`() {
        val instances = mutableListOf<(String, Int) -> Unit>()
        activity.setContent {
            tick.let { }
            instances += consumeAsync<String, Int> { a, b -> calls += "$a$b" }
        }
        idle()

        tick++
        idle()

        assertThat(instances.size).isAtLeast(2)
        instances.forEach { assertThat(it).isSameInstanceAs(instances.first()) }
    }

    @Test
    fun `Given a new block When consumeAsync with two args is invoked Then the new block receives the values`() {
        var action: ((String, Int) -> Unit)? = null
        activity.setContent {
            val version = blockVersion
            action = consumeAsync<String, Int> { a, b -> calls += "block$version:$a:$b" }
        }
        idle()

        blockVersion = 1
        idle()
        checkNotNull(action).invoke("x", 2)
        idle()

        assertThat(calls).containsExactly("block1:x:2")
    }

    private fun idle() = shadowOf(Looper.getMainLooper()).idle()
}
