@file:Suppress("DEPRECATION")

package com.raxdenstudios.commons.android.test

import androidx.test.espresso.Espresso.pressBack

@Deprecated(
    message = "Will be removed in the next major version. " +
        "Keep a project-owned page object base class instead.",
    level = DeprecationLevel.WARNING,
)
open class TestPage {

    companion object {
        inline fun <reified T : TestPage> on(): T = TestPage().on()
    }

    inline fun <reified T : TestPage> on(): T {
        val page = initPage<T>()
        page.verify()
        return page
    }

    inline fun <reified T : TestPage> initPage() = if (T::class.constructors.isEmpty()) {
        T::class.objectInstance!!
    } else {
        T::class.constructors.first().call()
    }

    open fun verify(): TestPage {
        // Each subpage should have its default assurances here
        return this
    }

    fun back(): TestPage {
        pressBack()
        return this
    }
}
