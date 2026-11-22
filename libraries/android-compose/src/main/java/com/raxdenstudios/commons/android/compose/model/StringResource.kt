package com.raxdenstudios.commons.android.compose.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import com.raxdenstudios.commons.core.ext.EMPTY

/**
 * Describes text for a composable without resolving it, so a ViewModel can emit it without a `Context`.
 * Call [toText] inside composition to get the final string.
 *
 * Every implementation is [Immutable]: once created, its values never change. Composables taking a
 * [StringResource] are therefore stable and skip recomposition when they receive an equal value. Keep this
 * contract: never pass a mutable list as [ResourceId.params] and mutate it afterwards.
 */
@Immutable
sealed interface StringResource {

    /**
     * A string resource. [params] are the format arguments of the resource; with none, the plain resource
     * text is used.
     *
     * @property resourceId The `R.string` id.
     * @property params Format arguments substituted into the resource, in order. Treated as read-only.
     */
    @Immutable
    data class ResourceId(
        @param:StringRes val resourceId: Int,
        val params: List<String> = emptyList(),
    ) : StringResource

    /** Text that is already resolved, e.g. user input or a server message. */
    @Immutable
    data class PlainText(
        val value: String,
    ) : StringResource

    /** No text; resolves to an empty string. */
    @Immutable
    data object Empty : StringResource

    /**
     * Resolves this to a `String` in composition, reading the string resource when needed. A formatted
     * resource is remembered per id, params and configuration, so it is not rebuilt on every recomposition.
     */
    @Composable
    @Suppress("SpreadOperator")
    fun toText(): String = when (this) {
        is ResourceId -> {
            if (params.isEmpty()) {
                stringResource(id = resourceId)
            } else {
                val resources = LocalResources.current
                val configuration = LocalConfiguration.current
                remember(resourceId, params, resources, configuration) {
                    resources.getString(resourceId, *params.toTypedArray())
                }
            }
        }

        is PlainText -> value

        is Empty -> String.EMPTY
    }
}
