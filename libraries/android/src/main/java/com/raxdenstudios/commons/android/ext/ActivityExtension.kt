@file:Suppress("TooManyFunctions")

package com.raxdenstudios.commons.android.ext

import android.app.Activity
import android.content.Intent
import android.os.Parcelable
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.raxdenstudios.commons.android.util.SDK

@Deprecated(
    message = "Will be removed in the next major version. " +
        "Use WindowInsets / ViewCompat.setOnApplyWindowInsetsListener to apply system bar insets.",
    level = DeprecationLevel.WARNING,
)
fun Activity.setVirtualNavigationBarSafeArea(
    view: View,
) {
    view.setPaddingRelative(
        view.paddingStart,
        view.paddingTop,
        view.paddingEnd,
        view.paddingBottom + SDK.getVirtualNavigationBarHeight(this),
    )
}

@Deprecated(
    message = "Will be removed in the next major version. " +
        "Use WindowInsets / ViewCompat.setOnApplyWindowInsetsListener to apply system bar insets.",
    level = DeprecationLevel.WARNING,
)
fun Activity.setStatusBarSafeArea(
    view: View,
) {
    view.setPaddingRelative(
        view.paddingStart,
        view.paddingTop + SDK.getStatusBarHeight(this),
        view.paddingEnd,
        view.paddingBottom,
    )
}

@Suppress("DEPRECATION")
fun Activity.setFullScreen() {
    window.decorView.apply {
        systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
    }
}

@Suppress("DEPRECATION")
fun <T : Parcelable> Activity.loadParcelable(
    key: String,
    defaultValue: T? = null,
    operation: (value: T) -> Unit,
) {
    val value = intent?.extras?.getParcelable(key) ?: defaultValue
    if (value != null) operation(value)
}

fun Activity.setResultOK() {
    setResult(Activity.RESULT_OK)
}

fun Activity.setResultOKAndFinish() {
    setResultOK()
    finish()
}

@Deprecated(
    message = "Will be removed in the next major version. " +
        "Use setResult with an explicit Intent or the Activity Result API.",
    level = DeprecationLevel.WARNING,
)
inline fun <reified T : Parcelable> Activity.setResultOKWithData(
    value: T,
) {
    setResult(
        Activity.RESULT_OK,
        Intent().apply { putExtra(T::class.java.simpleName, value) },
    )
}

@Deprecated(
    message = "Will be removed in the next major version. " +
        "Use setResult with an explicit Intent or the Activity Result API.",
    level = DeprecationLevel.WARNING,
)
@Suppress("DEPRECATION")
inline fun <reified T : Parcelable> Activity.setResultOKWithDataAndFinish(
    value: T,
) {
    setResultOKWithData(value)
    finish()
}

@Deprecated(
    message = "Will be removed in the next major version. " +
        "Use ViewBinding or setContent with Jetpack Compose.",
    level = DeprecationLevel.WARNING,
)
@Suppress("DEPRECATION")
fun Activity.contentView(
    @LayoutRes resId: Int,
) = com.raxdenstudios.commons.android.property.ActivityContentViewDelegate(resId)

@Deprecated(
    message = "Will be removed in the next major version. " +
        "Read the intent extras directly or use SavedStateHandle.",
    level = DeprecationLevel.WARNING,
)
@Suppress("DEPRECATION")
inline fun <reified T : Any> Activity.argument() =
    com.raxdenstudios.commons.android.property.ActivityArgumentDelegate<T>()

@Deprecated(
    message = "Will be removed in the next major version. " +
        "Use FragmentManager.findFragmentById directly.",
    level = DeprecationLevel.WARNING,
)
@Suppress("DEPRECATION")
fun <T : Fragment> Activity.fragmentById(
    @IdRes fragmentId: Int,
) = com.raxdenstudios.commons.android.property.FragmentActivityByIdDelegate<T>(fragmentId)
