package com.raxdenstudios.commons.core.ext

/**
 * Returns the items whose [selector] text contains [query], keeping their order. Returns this list unchanged
 * when [query] is blank, so an empty search box shows everything.
 *
 * [ignoreCase] applies to the comparison and defaults to `true`. Matching is substring-based, not fuzzy.
 *
 * ## Usage
 *
 * ```kotlin
 * val songs = allSongs.filterByQuery(query = searchText) { song -> song.title }
 * ```
 */
fun <T> List<T>.filterByQuery(
    query: String,
    ignoreCase: Boolean = true,
    selector: (T) -> String,
): List<T> = if (query.isBlank()) this else filter { item -> selector(item).contains(query, ignoreCase) }
