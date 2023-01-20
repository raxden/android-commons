package com.raxdenstudios.commons.ext

import com.google.android.material.appbar.AppBarLayout
import kotlin.math.abs

fun AppBarLayout.addOnScrollStateListener(onScrollState: (AppBarStateChangeListener.State) -> Unit) {
    addOnOffsetChangedListener(object : AppBarStateChangeListener() {
        override fun onStateChanged(appBarLayout: AppBarLayout, state: State) {
            onScrollState(state)
        }
    })
}

fun AppBarLayout.addOnScrollProgressListener(onScrollProgress: (Int) -> Unit) {
    addOnOffsetChangedListener(object : AppBarScrollProgressListener() {
        override fun onProgressChanged(appBarLayout: AppBarLayout, progress: Int) {
            onScrollProgress(progress)
        }
    })
}

abstract class AppBarScrollProgressListener : AppBarLayout.OnOffsetChangedListener {

    companion object {
        private const val FULL_PROGRESS = 100
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        val progress = abs(verticalOffset) * FULL_PROGRESS / appBarLayout.totalScrollRange
        onProgressChanged(appBarLayout, progress)
    }

    abstract fun onProgressChanged(appBarLayout: AppBarLayout, progress: Int)
}

abstract class AppBarStateChangeListener : AppBarLayout.OnOffsetChangedListener {

    enum class State { EXPANDED, COLLAPSED, IDLE }

    private var mCurrentState = State.IDLE

    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        mCurrentState = when {
            isExpanded(verticalOffset) -> {
                if (mCurrentState != State.EXPANDED) onStateChanged(appBarLayout, State.EXPANDED)
                State.EXPANDED
            }
            isCollapsed(verticalOffset, appBarLayout) -> {
                if (mCurrentState != State.COLLAPSED) onStateChanged(appBarLayout, State.COLLAPSED)
                State.COLLAPSED
            }
            else -> {
                if (mCurrentState != State.IDLE) onStateChanged(appBarLayout, State.IDLE)
                State.IDLE
            }
        }
    }

    private fun isCollapsed(
        verticalOffset: Int,
        appBarLayout: AppBarLayout
    ) = abs(verticalOffset) >= appBarLayout.totalScrollRange

    private fun isExpanded(verticalOffset: Int) = verticalOffset == 0

    abstract fun onStateChanged(appBarLayout: AppBarLayout, state: State)
}
