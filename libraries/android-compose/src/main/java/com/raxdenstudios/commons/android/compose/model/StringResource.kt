package com.raxdenstudios.commons.android.compose.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.raxdenstudios.commons.core.ext.EMPTY

/**
 * Describes text for a composable without resolving it, so a ViewModel can emit it without a `Context`.
 * Call [toText] inside composition to get the final string.
 */
sealed interface StringResource {

    /**
     * A string resource. [params] are the format arguments of the resource; with none, the plain resource
     * text is used.
     *
     * @property resourceId The `R.string` id.
     * @property params Format arguments substituted into the resource, in order.
     */
    data class ResourceId(
        @param:StringRes val resourceId: Int,
        val params: List<String> = emptyList(),
    ) : StringResource

    /** Text that is already resolved, e.g. user input or a server message. */
    data class PlainText(
        val value: String,
    ) : StringResource

    /** No text; resolves to an empty string. */
    data object Empty : StringResource

    /** Resolves this to a `String` in composition, reading the string resource when needed. */
    @Composable
    @Suppress("SpreadOperator")
    fun toText(): String = when (this) {
        is ResourceId -> {
            if (params.isEmpty()) {
                stringResource(id = resourceId)
            } else {
                stringResource(id = resourceId, *params.toTypedArray())
            }
        }

        is PlainText -> value

        is Empty -> String.EMPTY
    }
}
