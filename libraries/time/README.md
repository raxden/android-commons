# Time Commons

`java.time` extensions for epoch conversion, formatting and today checks. No initialisation is needed.

## Installation

```kotlin
dependencies {
    implementation(platform("com.raxdenstudios:commons-bom:<latest-version>"))
    implementation("com.raxdenstudios:commons-time")
}
```

## Features

### Epoch conversion

- `LocalDate.toMilliseconds(timeZone)` and `toSeconds(timeZone)`: the start of the day in the zone.
- `LocalDateTime.toMilliseconds(timeZone)` and `toSeconds(timeZone)`: the date-time in the zone.

### Formatting

- `LocalDate.toFormat(pattern)`: pattern-based, default `dd MMM yyyy`.
- `LocalDate.toLocalizedString(style, locale)`: localized style, e.g. "March 15, 2024" for `FormatStyle.LONG` in en-US.
- `LocalDateTime.toFormat(pattern)`: pattern-based, default `dd MMM yyyy`.
- `LocalDateTime.toHourMinutes()`: 24-hour `HH:mm`, e.g. `"09:05"`.

```kotlin
LocalDate.of(2024, 3, 15).toLocalizedString()                  // "March 15, 2024" in en-US
LocalDateTime.of(2024, 3, 15, 9, 5).toHourMinutes()            // "09:05"
```

### Checks

- `LocalDate.isToday()`: compares with `LocalDate.now()` in the system default zone.

## License

See the [LICENSE](../../LICENSE.txt) file for details.
