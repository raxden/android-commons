# Core Commons

Kotlin and JVM helpers with no Android dependency: string, list, number, locale and `Answer` utilities.

## Installation

```kotlin
dependencies {
    implementation(platform("com.raxdenstudios:commons-bom:<latest-version>"))
    implementation("com.raxdenstudios:commons-core")
}
```

## Features

### Strings

In `com.raxdenstudios.commons.core.ext`:

- `String.removeDiacritics()`: "Canción" becomes "Cancion".
- `String.alphanumericOnly()`: keeps only ASCII letters and digits. It also drops non-ASCII letters, so call `removeDiacritics()` first.
- `String.collapseWhitespace()`: trims and replaces runs of whitespace with one space.
- `String.toFlagEmoji()`: the flag for an ISO 3166-1 alpha-2 code, e.g. `"es"`. Returns `""` for any other input.

```kotlin
"Canción".removeDiacritics().alphanumericOnly()  // "Cancion"
"fr".toFlagEmoji()                               // flag of France
"ESP".toFlagEmoji()                              // "" (not a two-letter code)
```

### Lists

- `List<T>.filterByQuery(query, ignoreCase = true) { item -> text }`: keeps the items whose text contains `query`. A blank query returns the list unchanged.

### Numbers

- `Double.toCurrencyString(currency, locale)`: formats money, e.g. `"$1,234.50"` for USD in en-US.

### Locales

- `Locale.displayLanguageName(inLocale)`: the language name written in `inLocale`, e.g. "Spanish" for `es-ES` in English.

## Deprecated (removed in next major)

Marked `@Deprecated` (warning level) and scheduled for removal in the next major version. Nothing was removed and no signature changed.

- `Random.generateStringWithSize`, `Locale.displayLanguageName` (use `getDisplayLanguage(inLocale)`), `Double.toCurrencyString` (use `NumberFormat.getCurrencyInstance`).
- `List.replaceItem` (use `map`) and `List.removeItem` (use `filterNot`).
- `String.ifEmptyThen` (use `ifEmpty { }`) and `String?.ifEmptyOrNullThen` (use `orEmpty().ifEmpty { }`).
- `applyWhen` and `alsoWhen` (`AnyExtension`): use an `if` expression.
- `Answer.flatMapFailure`.

## License

See the [LICENSE](../../LICENSE.txt) file for details.
