package com.raxdenstudios.commons.android.ext

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.raxdenstudios.commons.android.property.FragmentArgumentDelegate
import com.raxdenstudios.commons.android.property.FragmentByIdDelegate

fun Fragment.inflateView(
    layoutId: Int,
    root: ViewGroup? = null
): View = layoutInflater.inflate(layoutId, root)

fun Fragment.close() = activity?.closeFragment(this)

inline fun <reified TFragment : Fragment> Fragment.loadFragment(
    containerView: View,
    savedInstanceState: Bundle?,
    create: () -> TFragment
): TFragment = savedInstanceState?.let {
    childFragmentManager.findFragmentById(containerView.id) as? TFragment
} ?: create().also { fragment ->
    childFragmentManager.commit {
        replace(
            containerView.id,
            fragment,
            fragment.javaClass.simpleName
        )
    }
}

inline fun <reified TFragment : DialogFragment> Fragment.loadDialogFragment(
    tag: String,
    isCancelable: Boolean = false,
    createFragment: () -> TFragment
): TFragment {
    val dialogFragment = childFragmentManager.findFragmentByTag(tag) as? TFragment
        ?: createFragment().also { fragment ->
            childFragmentManager.commit(true) { add(fragment, tag) }
        }
    dialogFragment.isCancelable = isCancelable
    return dialogFragment
}

fun <T : Any> Fragment.argument() = FragmentArgumentDelegate<T>()

fun <T : Fragment> Fragment.fragmentById(
    @IdRes fragmentId: Int
) = FragmentByIdDelegate<T>(fragmentId)
