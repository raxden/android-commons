package com.raxdenstudios.commons.android.ext

import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner

fun DialogFragment.setFullscreen() = dialog?.window?.setLayout(
    ViewGroup.LayoutParams.MATCH_PARENT,
    ViewGroup.LayoutParams.MATCH_PARENT,
)

/**
 * Shows this dialog with [tag] and delivers the result it sets for [requestKey] to [onFragmentResult].
 *
 * The listener is bound to [lifecycleOwner], the host that shows the dialog (for example a fragment's
 * `viewLifecycleOwner` or the activity): results are delivered while it is started and the listener is
 * removed when it is destroyed. Does nothing if a fragment with [tag] is already shown.
 */
@Deprecated(
    message = "Will be removed in the next major version. " +
        "Use FragmentManager.setFragmentResultListener with DialogFragment.show directly.",
    level = DeprecationLevel.WARNING,
)
fun DialogFragment.showForResult(
    fragmentManager: FragmentManager,
    lifecycleOwner: LifecycleOwner,
    tag: String,
    requestKey: String = tag,
    onFragmentResult: (result: Bundle) -> Unit,
) {
    if (fragmentManager.findFragmentByTag(tag) != null) return
    fragmentManager.setFragmentResultListener(
        requestKey,
        lifecycleOwner,
    ) { resultRequestKey, result ->
        if (requestKey != resultRequestKey) return@setFragmentResultListener
        onFragmentResult(result)
    }
    show(fragmentManager, tag)
}
