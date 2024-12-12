package com.raxdenstudios.commons.rx.test.rules

import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class RxSchedulerRule : TestWatcher() {

    companion object {
        private val SCHEDULER_INSTANCE = Schedulers.trampoline()
    }

    override fun starting(description: Description) {
        super.starting(description)
        RxAndroidPlugins.reset()
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { SCHEDULER_INSTANCE }

        RxJavaPlugins.reset()
        RxJavaPlugins.setIoSchedulerHandler { SCHEDULER_INSTANCE }
        RxJavaPlugins.setNewThreadSchedulerHandler { SCHEDULER_INSTANCE }
        RxJavaPlugins.setComputationSchedulerHandler { SCHEDULER_INSTANCE }
    }
}
