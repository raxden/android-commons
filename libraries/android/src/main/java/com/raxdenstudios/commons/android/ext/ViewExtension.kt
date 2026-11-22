@file:Suppress("TooManyFunctions")

package com.raxdenstudios.commons.android.ext

import android.content.res.TypedArray
import android.os.SystemClock
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.annotation.AttrRes
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import androidx.annotation.StyleableRes
import androidx.core.content.ContextCompat

fun View.setPaddingTop(
    padding: Int,
) {
    setPaddingRelative(
        paddingStart,
        padding,
        paddingEnd,
        paddingBottom,
    )
}

fun View.doItSelectable() {
    isSelected = true
}

fun View.doItUnselectable() {
    isSelected = false
}

fun View.doItClickable() {
    isClickable = true
}

fun View.doItNonClickable() {
    isClickable = false
}

fun View.visibleGone(
    visible: Boolean,
) {
    visibility = if (visible) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

fun View.isVisible() = visibility == View.VISIBLE

fun View.isGone() = visibility == View.GONE

fun View.isInvisible() = visibility == View.INVISIBLE

fun View.getColor(
    resId: Int,
) = ContextCompat.getColor(context, resId)

fun View.getDrawable(
    resId: Int,
) = ContextCompat.getDrawable(context, resId)

fun ViewGroup.inflateView(
    @LayoutRes layoutRes: Int,
    attachToRoot: Boolean = false,
): View = LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)

fun ViewGroup.inflateViewIfEditMode(
    @LayoutRes layoutRes: Int,
    attachToRoot: Boolean = false,
) {
    if (isInEditMode) inflateView(layoutRes, attachToRoot)
}

fun View.startFadeInAnimation() {
    startAnimation(AnimationUtils.loadAnimation(context, android.R.anim.fade_in))
}

fun View.startFadeOutAnimation() {
    startAnimation(AnimationUtils.loadAnimation(context, android.R.anim.fade_out))
}

// Use coroutines|rx boundary...
fun View.setSafeOnClickListener(
    debounceTime: Long = 500,
    onSafeClick: (View) -> Unit,
) {
    var lastTimeClicked: Long = 0
    setOnClickListener {
        if (SystemClock.elapsedRealtime() - lastTimeClicked >= debounceTime) {
            lastTimeClicked = SystemClock.elapsedRealtime()
            onSafeClick(it)
        }
    }
}

fun View.useStyledAttributes(
    attrs: AttributeSet?,
    @StyleableRes styleableResId: IntArray,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0,
    block: (TypedArray) -> Unit,
) {
    val typedArray =
        context.obtainStyledAttributes(attrs, styleableResId, defStyleAttr, defStyleRes)
    block(typedArray)
    typedArray.recycle()
}
