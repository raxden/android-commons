package com.raxdenstudios.commons.android.ext

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit

fun Fragment.inflateView(
    layoutId: Int,
    root: ViewGroup? = null,
): View = layoutInflater.inflate(layoutId, root)

fun Fragment.close() = activity?.closeFragment(this)

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

inline fun <reified TFragment : DialogFragment> Fragment.loadDialogFragment(
    tag: String,
    isCancelable: Boolean = false,
    createFragment: () -> TFragment,
): TFragment {
    val dialogFragment = childFragmentManager.findFragmentByTag(tag) as? TFragment
        ?: createFragment().also { fragment ->
            childFragmentManager.commit(true) { add(fragment, tag) }
        }
    dialogFragment.isCancelable = isCancelable
    return dialogFragment
}

@Deprecated(
    message = "Will be removed in the next major version. " +
        "Read the fragment arguments directly or use SavedStateHandle.",
    level = DeprecationLevel.WARNING,
)
@Suppress("DEPRECATION")
fun <T : Any> Fragment.argument() = com.raxdenstudios.commons.android.property.FragmentArgumentDelegate<T>()

@Deprecated(
    message = "Will be removed in the next major version. " +
        "Use FragmentManager.findFragmentById directly.",
    level = DeprecationLevel.WARNING,
)
@Suppress("DEPRECATION")
fun <T : Fragment> Fragment.fragmentById(
    @IdRes fragmentId: Int,
) = com.raxdenstudios.commons.android.property.FragmentByIdDelegate<T>(fragmentId)
