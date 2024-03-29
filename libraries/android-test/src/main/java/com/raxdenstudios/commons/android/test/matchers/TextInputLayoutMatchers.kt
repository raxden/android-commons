package com.raxdenstudios.commons.android.test.matchers

import android.view.View
import com.google.android.material.textfield.TextInputLayout
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

object TextInputLayoutMatchers {

    fun hasNoTextInputLayoutError() = object : TypeSafeMatcher<View>() {

        @Suppress("EmptyFunctionBlock")
        override fun describeTo(description: Description) {
        }

        override fun matchesSafely(view: View): Boolean {
            return !hasTextInputLayoutErrorText(view, "")
        }
    }

    fun hasTextInputLayoutErrorText(expectedError: Int) = object : TypeSafeMatcher<View>() {

        @Suppress("EmptyFunctionBlock")
        override fun describeTo(description: Description) {
        }

        override fun matchesSafely(view: View): Boolean {
            val expectedErrorText = view.resources.getString(expectedError)
            return hasTextInputLayoutErrorText(view, expectedErrorText)
        }
    }

    fun hasTextInputLayoutErrorText(expectedErrorText: String) = object : TypeSafeMatcher<View>() {

        @Suppress("EmptyFunctionBlock")
        override fun describeTo(description: Description) {
        }

        override fun matchesSafely(view: View): Boolean {
            return hasTextInputLayoutErrorText(view, expectedErrorText)
        }
    }

    private fun hasTextInputLayoutErrorText(view: View, expectedErrorText: String): Boolean {
        val textInputLayout = (view as? TextInputLayout) ?: return false
        return hasTextInputLayoutErrorText(textInputLayout, expectedErrorText)
    }

    private fun hasTextInputLayoutErrorText(
        textInputLayout: TextInputLayout,
        expectedErrorText: String
    ): Boolean {
        val error = textInputLayout.error ?: return false
        return error.toString() == expectedErrorText
    }
}

