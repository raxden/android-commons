package com.raxdenstudios.commons.core.ext

@Deprecated(
    message = "Will be removed in the next major version. " +
        "Use List.map with a conditional instead.",
    level = DeprecationLevel.WARNING,
)
fun <T> List<T>.replaceItem(
    newValue: T,
    condition: (T) -> Boolean,
): List<T> = map { list ->
    if (condition(list)) newValue else list
}

@Deprecated(
    message = "Will be removed in the next major version. " +
        "Use List.filterNot instead.",
    replaceWith = ReplaceWith("filterNot(condition)"),
    level = DeprecationLevel.WARNING,
)
fun <T> List<T>.removeItem(
    condition: (T) -> Boolean,
): List<T> = filterNot(condition)

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
