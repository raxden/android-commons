# Android Room Commons

A library with a set of useful classes to help developers work with Room persistence library.

## Installation

```kotlin
dependencies {
    implementation("com.raxdenstudios:commons-android-room:{version}")
}
```

## Features

- `BaseDao<T>`: a generic DAO interface to extend from your own DAOs. It provides:
  - `insert(type)` / `insert(types)`: insert with `OnConflictStrategy.REPLACE`, returning the row id(s).
  - `insertIgnore(type)` / `insertIgnore(types)`: insert with `OnConflictStrategy.IGNORE`, returning the row id(s).
  - `update(type)` / `update(types)`: update, returning the number of rows updated.
  - `delete(type)` / `delete(types)`: delete, returning the number of rows deleted.
  - `upsert(types)`: Room `@Upsert` for a list.
  - `upsert(type)`: Room `@Upsert` for one entity. An existing row is updated in place (not deleted and re-inserted), so `ON DELETE CASCADE` children are kept.

All methods are `suspend`. The module exposes `room-runtime` and `room-ktx` as `api` dependencies. It does not provide type converters. Consumers still need to add the Room compiler themselves, for example `ksp("androidx.room:room-compiler:{version}")`.

## License

See the [LICENSE](../../LICENSE.txt) file for details.
