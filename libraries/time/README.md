# Time Commons

`java.time` extensions for epoch conversion and time formatting. No initialisation is needed.

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

- `LocalDateTime.toHourMinutes()`: 24-hour `HH:mm`, e.g. `"09:05"`.
- `LocalTime.toShortText(locale)`: localized short time, e.g. `"09:05"` in en-GB or `"9:05 AM"` in en-US.
- `DayOfWeek.shortName(locale)` and `DayOfWeek.narrowName(locale)`: abbreviated and narrowest day names, e.g. `"Mon"` and `"M"` in English.

- `Duration.toClockText(padMinutes = true)` (`kotlin.time.Duration`): elapsed time as `mm:ss` below one hour and `h:mm:ss` from one hour. Whole seconds are kept (truncated), negative durations show as `00:00`, and digits are always ASCII (`Locale.ROOT`), whatever the device locale. From one hour the format switches to `h:mm:ss`, so 90 minutes is `1:30:00`, not `90:00`. With `padMinutes = false` the leading zero of minutes below one hour is dropped (`"1:35"`, `"0:05"`); hours are unchanged.

```kotlin
LocalDateTime.of(2024, 3, 15, 9, 5).toHourMinutes()            // "09:05"
LocalTime.of(9, 5).toShortText(Locale.US)                      // "9:05 AM"
DayOfWeek.MONDAY.shortName(Locale.US)                          // "Mon"
95.seconds.toClockText()                                       // "01:35"
95.seconds.toClockText(padMinutes = false)                     // "1:35"
(1.hours + 2.minutes + 3.seconds).toClockText()                // "1:02:03"
```

## Removed in 2.0.0

- `LocalDate.isToday()`: compare with `LocalDate.now()`.
- `LocalDate.toFormat()` and `LocalDateTime.toFormat()`: use `format` with `DateTimeFormatter.ofPattern`.
- `LocalDate.toLocalizedString()`: use `format` with `DateTimeFormatter.ofLocalizedDate`.

## License

See the [LICENSE](../../LICENSE.txt) file for details.
