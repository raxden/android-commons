# Core Commons

Kotlin and JVM helpers with no Android dependency: string, list and `Answer` utilities.

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
- `String.toDisplayCountry(locale)`: the country name for a region code, e.g. `"ES"` becomes `"Spain"` (or `"España"` in Spanish). Returns `null` for an invalid or blank code, or when the platform has no name for it.
- `String.capitalized(locale)`: upper-cases the first character with the titlecase rules of `locale` and keeps the rest. Replaces the deprecated `capitalize`.

```kotlin
"Canción".removeDiacritics().alphanumericOnly()  // "Cancion"
"fr".toFlagEmoji()                               // flag of France
"ESP".toFlagEmoji()                              // "" (not a two-letter code)
"ES".toDisplayCountry(Locale.US)                 // "Spain"
"XX".toDisplayCountry(Locale.US)                 // null (no name for the code)
"hola".capitalized()                             // "Hola"
```

### Lists

- `List<T>.filterByQuery(query, ignoreCase = true) { item -> text }`: keeps the items whose text contains `query`. A blank query returns the list unchanged.

## Removed in 6.0.0

- `Random.generateStringWithSize`: use `kotlin.random.Random` with a character pool.
- `Locale.displayLanguageName`: use `getDisplayLanguage(inLocale)`.
- `Double.toCurrencyString`: use `NumberFormat.getCurrencyInstance`.
- `List.replaceItem`: use `map`.
- `List.removeItem`: use `filterNot`.
- `String.ifEmptyThen`: use `ifEmpty { }`.
- `String?.ifEmptyOrNullThen`: use `orEmpty().ifEmpty { }`.
- `applyWhen` and `alsoWhen` (`AnyExtension`): use an `if` expression.
- `Answer.flatMapFailure`: use a `when` expression over `Answer`.

## License

See the [LICENSE](../../LICENSE.txt) file for details.
