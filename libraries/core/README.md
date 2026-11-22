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

```kotlin
"Canción".removeDiacritics().alphanumericOnly()  // "Cancion"
"fr".toFlagEmoji()                               // flag of France
"ESP".toFlagEmoji()                              // "" (not a two-letter code)
```

### Lists

- `List<T>.filterByQuery(query, ignoreCase = true) { item -> text }`: keeps the items whose text contains `query`. A blank query returns the list unchanged.

## Removed in 7.0.0

- `Random.generateStringWithSize`: use `kotlin.random.Random` with a character pool.
- `Locale.displayLanguageName`: use `getDisplayLanguage(inLocale)`.
- `Double.toCurrencyString`: use `NumberFormat.getCurrencyInstance`.
- `List.replaceItem`: use `map`.
- `List.removeItem`: use `filterNot`.
- `String.ifEmptyThen`: use `ifEmpty { }`.
- `String?.ifEmptyOrNullThen`: use `orEmpty().ifEmpty { }`.
- `applyWhen` and `alsoWhen` (`AnyExtension`): use an `if` expression.
- `Answer.flatMapFailure`: use a `when` expression over `Answer`.

## Removed in 8.0.0

- `AnyExtension`: `castAs`, `requireAs`, `letAs`, `runAs`, `applyAs`, `alsoAs`: use `as?` / `as` with the standard scope functions.
- `String.toMD5()` and `String.toSHA512()`: use `MessageDigest.getInstance` directly. `toSHA256` is kept.
- `orDefault` for `Byte?`, `Short?`, `Long?` and `Float?`, and `ZERO` for `Byte`, `Short`, `Long`, `Float`: use the elvis operator. `Boolean?`, `Int?`, `Double?` and `String?` `orDefault` and `Int`/`Double` `ZERO` are kept.

## License

See the [LICENSE](../../LICENSE.txt) file for details.
