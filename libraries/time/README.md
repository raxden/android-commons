# Time Commons

`java.time` extension for epoch conversion. No initialisation is needed.

## Installation

```kotlin
dependencies {
    implementation(platform("com.raxdenstudios:commons-bom:<latest-version>"))
    implementation("com.raxdenstudios:commons-time")
}
```

## Features

### Epoch conversion

- `LocalDateTime.toMilliseconds(timeZone)`: the milliseconds since the Unix epoch of the date-time in the zone.

```kotlin
LocalDateTime.now().toMilliseconds()
```

## Removed in 2.0.0

- `LocalDate.isToday()`: compare with `LocalDate.now()`.
- `LocalDate.toFormat()` and `LocalDateTime.toFormat()`: use `format` with `DateTimeFormatter.ofPattern`.
- `LocalDate.toLocalizedString()`: use `format` with `DateTimeFormatter.ofLocalizedDate`.

## Removed in 3.0.0

- `LocalDate.toMilliseconds()` and `LocalDate.toSeconds()`: use `atStartOfDay().atZone(zone).toInstant().toEpochMilli()`.
- `LocalDateTime.toSeconds()`: use `toMilliseconds() / 1000` (floor division).
- `LocalDateTime.toHourMinutes()`: use `format` with `DateTimeFormatter.ofPattern("HH:mm")`.
- `String.toLocalDate(patterns)` and `String.toLocalDate(formatters)`: use `LocalDate.parse` with a `DateTimeFormatter`.
- `Long.toLocalDate()` and `Long.toLocalDateTime()`: use `Instant.ofEpochMilli` with `atZone`.

## License

See the [LICENSE](../../LICENSE.txt) file for details.
