package com.raxdenstudios.commons.android.ext

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.commitNow

fun Fragment.inflateView(
    layoutId: Int,
    root: ViewGroup? = null,
): View = layoutInflater.inflate(layoutId, root)

fun Fragment.close() = activity?.closeFragment(this)

/**
 * Returns the [TFragment] already attached to [containerView] when [savedInstanceState] is non-null and
 * that fragment is a [TFragment]. Otherwise creates one with [create] and replaces the container's content
 * with it, committed asynchronously.
 */
inline fun <reified TFragment : Fragment> Fragment.loadFragment(
    containerView: View,
    savedInstanceState: Bundle?,
    create: () -> TFragment,
): TFragment = savedInstanceState?.let {
    childFragmentManager.findFragmentById(containerView.id) as? TFragment
} ?: create().also { fragment ->
    childFragmentManager.commit {
        replace(
            containerView.id,
            fragment,
            fragment.javaClass.simpleName,
        )
    }
}

/**
 * Returns the [TFragment] shown with [tag] in the child fragment manager. When none exists, creates it with
 * [createFragment] and adds it synchronously with `commitNow`. Back-to-back calls with the same [tag]
 * return the same instance. [isCancelable] is applied on every call.
 *
 * Do not call this while the child FragmentManager is executing transactions: `commitNow` then throws an
 * [IllegalStateException]. The add uses `allowStateLoss = true`, so it may be lost if the state is already saved.
 */
inline fun <reified TFragment : DialogFragment> Fragment.loadDialogFragment(
    tag: String,
    isCancelable: Boolean = false,
    createFragment: () -> TFragment,
): TFragment {
    val dialogFragment = childFragmentManager.findFragmentByTag(tag) as? TFragment
        ?: createFragment().also { fragment ->
            childFragmentManager.commitNow(allowStateLoss = true) { add(fragment, tag) }
        }
    dialogFragment.isCancelable = isCancelable
    return dialogFragment
}
