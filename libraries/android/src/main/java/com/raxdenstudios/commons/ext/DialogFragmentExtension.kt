package com.raxdenstudios.commons.ext

import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

fun DialogFragment.setFullscreen() =
    dialog?.window?.setLayout(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    )

fun DialogFragment.showForResult(
    fragmentManager: FragmentManager,
    tag: String,
    requestKey: String = tag,
    onFragmentResult: (result: Bundle) -> Unit
) {
    if (fragmentManager.findFragmentByTag(tag) != null) return
    fragmentManager.setFragmentResultListener(
        requestKey,
        viewLifecycleOwner
    ) { resultRequestKey, result ->
        if (requestKey != resultRequestKey) return@setFragmentResultListener
        onFragmentResult(result)
    }
    show(fragmentManager, tag)
}
