package com.raxdenstudios.commons.android.ext

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.Operation
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
internal class WorkManagerExtensionTest {

    private val workManager: WorkManager = mockk()
    private val operation: Operation = mockk()
    private val nameSlot = slot<String>()
    private val policySlot = slot<ExistingWorkPolicy>()
    private val requestSlot = slot<OneTimeWorkRequest>()

    @Before
    fun setUp() {
        every {
            workManager.enqueueUniqueWork(capture(nameSlot), capture(policySlot), capture(requestSlot))
        } returns operation
    }

    @Test
    fun `Given default arguments, When enqueueUniqueOneTime is called, Then use worker class name and KEEP policy`() {
        val result = workManager.enqueueUniqueOneTime<TestWorker>()

        assertThat(result).isSameInstanceAs(operation)
        assertThat(nameSlot.captured).isEqualTo(TestWorker::class.java.name)
        assertThat(policySlot.captured).isEqualTo(ExistingWorkPolicy.KEEP)
        assertThat(requestSlot.captured.workSpec.workerClassName).isEqualTo(TestWorker::class.java.name)
    }

    @Test
    fun `Given custom name and policy, When enqueueUniqueOneTime is called, Then they are forwarded`() {
        workManager.enqueueUniqueOneTime<TestWorker>(
            policy = ExistingWorkPolicy.REPLACE,
            uniqueName = "custom",
        )

        assertThat(nameSlot.captured).isEqualTo("custom")
        assertThat(policySlot.captured).isEqualTo(ExistingWorkPolicy.REPLACE)
    }

    @Test
    fun `Given a configure block, When enqueueUniqueOneTime is called, Then it is applied to the request`() {
        workManager.enqueueUniqueOneTime<TestWorker> { addTag("my-tag") }

        assertThat(requestSlot.captured.tags).contains("my-tag")
    }

    class TestWorker(
        context: Context,
        params: WorkerParameters,
    ) : Worker(context, params) {

        override fun doWork(): Result = Result.success()
    }
}
