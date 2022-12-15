package com.raxdenstudios.commons.android.test.matchers

import android.view.View
import android.widget.Checkable
import androidx.recyclerview.widget.RecyclerView
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

object RecyclerViewMatchers {

    fun hasRecyclerViewItemCount(itemCount: Int) = object : TypeSafeMatcher<View>() {

        @Suppress("EmptyFunctionBlock")
        override fun describeTo(description: Description) {
        }

        override fun matchesSafely(view: View): Boolean {
            val recyclerView = view as? RecyclerView ?: return false
            return recyclerView.adapter?.itemCount == itemCount
        }
    }

    fun checkBoxState(itemCount: Int, checkBoxId: Int, isChecked: Boolean) =
        object : TypeSafeMatcher<View>() {

            @Suppress("EmptyFunctionBlock")
            override fun describeTo(description: Description) {
            }

            override fun matchesSafely(view: View): Boolean {
                val recyclerView = view as? RecyclerView ?: return false
                return matchesSafely(recyclerView)
            }

            private fun matchesSafely(recyclerView: RecyclerView): Boolean {
                val viewHolder =
                    recyclerView.findViewHolderForAdapterPosition(itemCount) ?: return false
                return matchesSafely(viewHolder)
            }

            private fun matchesSafely(viewHolder: RecyclerView.ViewHolder): Boolean {
                val view = viewHolder.itemView.findViewById<View>(checkBoxId)
                val checkableView = view as? Checkable ?: return false
                return matchesSafely(checkableView)
            }

            private fun matchesSafely(checkableView: Checkable): Boolean {
                return checkableView.isChecked == isChecked
            }
        }
}
