package com.raxdenstudios.commons.android.test.matchers

import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

object SwipeRefreshLayoutMatchers {

    fun isSwipeRefreshLayoutRefreshing() = object : TypeSafeMatcher<View>() {

        @Suppress("EmptyFunctionBlock")
        override fun describeTo(description: Description) {
        }

        override fun matchesSafely(view: View): Boolean {
            val swipeRefreshLayout = (view as? SwipeRefreshLayout) ?: return false
            return swipeRefreshLayout.isRefreshing
        }
    }
}
