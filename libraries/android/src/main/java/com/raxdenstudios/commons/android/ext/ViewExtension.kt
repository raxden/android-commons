@file:Suppress("TooManyFunctions")

package com.raxdenstudios.commons.android.ext

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.os.SystemClock
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.AttrRes
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import androidx.annotation.StyleableRes
import androidx.core.content.ContextCompat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

fun View.setPaddingTop(padding: Int) {
    setPaddingRelative(
        paddingStart,
        padding,
        paddingEnd,
        paddingBottom
    )
}

fun View.onFocusGained(onFocusGained: (View) -> Unit = {}) {
    onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
        if (hasFocus) onFocusGained(view)
    }
}

fun View.onFocusLost(onLostFocus: (View) -> Unit = {}) {
    onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
        if (!hasFocus) onLostFocus(view)
    }
}

@SuppressLint("ClickableViewAccessibility")
fun View.closeKeyboardWhenTouchScreen() {
    // Set up touch listener for non-text box views to hide keyboard.
    if (this !is EditText) {
        setOnTouchListener { _, _ ->
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(windowToken, 0)
            false
        }
    }
    //If a layout container, iterate over children and seed recursion.
    if (this is ViewGroup) {
        for (i in 0 until childCount) {
            getChildAt(i).closeKeyboardWhenTouchScreen()
        }
    }
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

fun View.visibleGone(visible: Boolean) {
    visibility = if (visible) View.VISIBLE
    else View.GONE
}

fun View.isVisible() = visibility == View.VISIBLE

fun View.doItVisible() {
    if (visibility != View.VISIBLE) visibility = View.VISIBLE
}

fun View.doItGone() {
    if (visibility != View.GONE) visibility = View.GONE
}

fun View.isGone() = visibility == View.GONE

fun View.doItInvisible() {
    if (visibility != View.INVISIBLE) visibility = View.INVISIBLE
}

fun View.isInvisible() = visibility == View.INVISIBLE

fun View.getColor(resId: Int) = ContextCompat.getColor(context, resId)

fun View.getDrawable(resId: Int) = ContextCompat.getDrawable(context, resId)

fun ViewGroup.inflateView(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View =
    LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)

fun ViewGroup.inflateViewIfEditMode(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false) {
    if (isInEditMode) inflateView(layoutRes, attachToRoot)
}

fun View.startFadeInAnimation() {
    startAnimation(AnimationUtils.loadAnimation(context, android.R.anim.fade_in))
}

fun View.startFadeOutAnimation() {
    startAnimation(AnimationUtils.loadAnimation(context, android.R.anim.fade_out))
}

// Use coroutines|rx boundary...
fun View.setSafeOnClickListener(debounceTime: Long = 500, onSafeClick: (View) -> Unit) {
    var lastTimeClicked: Long = 0
    setOnClickListener {
        if (SystemClock.elapsedRealtime() - lastTimeClicked >= debounceTime) {
            lastTimeClicked = SystemClock.elapsedRealtime()
            onSafeClick(it)
        }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
fun View.clicks(): Flow<Unit> = callbackFlow {
    setOnClickListener {
        trySend(Unit).isSuccess
    }
    awaitClose { setOnClickListener(null) }
}

fun View.useStyledAttributes(
    attrs: AttributeSet?,
    @StyleableRes styleableResId: IntArray,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0,
    block: (TypedArray) -> Unit
) {
    val typedArray =
        context.obtainStyledAttributes(attrs, styleableResId, defStyleAttr, defStyleRes)
    block(typedArray)
    typedArray.recycle()
}
