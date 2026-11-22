package com.raxdenstudios.commons.android.ext

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import androidx.fragment.app.commitNow

/**
 * Returns the [TFragment] already attached to [containerView] when [savedInstanceState] is non-null and
 * that fragment is a [TFragment]. Otherwise creates one with [create] and replaces the container's content
 * with it, committed asynchronously.
 */
inline fun <reified TFragment : Fragment> FragmentActivity.loadFragment(
    containerView: View,
    savedInstanceState: Bundle?,
    create: () -> TFragment,
): TFragment = savedInstanceState?.let {
    supportFragmentManager.findFragmentById(containerView.id) as? TFragment
} ?: create().also { fragment ->
    supportFragmentManager.commit {
        replace(containerView.id, fragment, fragment.javaClass.simpleName)
    }
}

fun FragmentActivity.closeFragment(
    fragment: Fragment,
) {
    supportFragmentManager.commit { remove(fragment) }
}

/**
 * Returns the [TFragment] shown with [tag] in the support fragment manager. When none exists, creates it with
 * [create] and adds it synchronously with `commitNow`. Back-to-back calls with the same [tag] return the
 * same instance. [isCancelable] is applied on every call.
 *
 * Do not call this while the support FragmentManager is executing transactions: `commitNow` then throws an
 * [IllegalStateException]. The add uses `allowStateLoss = true`, so it may be lost if the state is already saved.
 */
inline fun <reified TFragment : DialogFragment> FragmentActivity.loadDialogFragment(
    tag: String,
    isCancelable: Boolean = false,
    create: () -> TFragment,
): TFragment {
    val dialogFragment =
        supportFragmentManager.findFragmentByTag(tag) as? TFragment ?: create().also { fragment ->
            supportFragmentManager.commitNow(allowStateLoss = true) { add(fragment, tag) }
        }
    dialogFragment.isCancelable = isCancelable
    return dialogFragment
}
