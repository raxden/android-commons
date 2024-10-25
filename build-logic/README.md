# Convention Plugins

The `build-logic` folder defines project-specific convention plugins, used to keep a single
source of truth for common module configurations.

This approach is heavily based on
[https://developer.squareup.com/blog/herding-elephants/](https://developer.squareup.com/blog/herding-elephants/)
and
[https://github.com/jjohannes/idiomatic-gradle](https://github.com/jjohannes/idiomatic-gradle).

By setting up convention plugins in `build-logic`, we can avoid duplicated build script setup,
messy `subproject` configurations, without the pitfalls of the `buildSrc` directory.

`build-logic` is an included build, as configured in the root
[`settings.gradle.kts`](../settings.gradle.kts).

Inside `build-logic` is a `convention` module, which defines a set of plugins that all normal
modules can use to configure themselves.

`build-logic` also includes a set of `Kotlin` files used to share logic between plugins themselves,
which is most useful for configuring Android components (libraries vs applications) with shared
code.

These plugins are *additive* and *composable*, and try to only accomplish a single responsibility.
Modules can then pick and choose the configurations they need.
If there is one-off logic for a module without shared code, it's preferable to define that directly
in the module's `build.gradle`, as opposed to creating a convention plugin with module-specific
setup.

## Basic setup

1. Navigate to Your Project's Root Directory
2. Add the Repository as a Submodule:

```
git submodule add git@github.com:raxden/android-convention.git
```
3. Change the name of the folder to build-logic
```
git mv android-convention build-logic
```
4. Modify your `settings.gradle.kts` setting the path of toml.
```
pluginManagement {
    includeBuild("build-logic")
    ...
}
...
dependencyResolutionManagement {
    versionCatalogs {
        create("libs") { from(files("build-logic/gradle/libraries.versions.toml")) }
    }
    ...
}
```

## Plugins

#### Android application

<details open><summary>Kotlin</summary>

```kt
plugins {
    id("com.raxdenstudios.android-application")
}
```

</details>

<details><summary>Groovy</summary>

```groovy
plugins {
    id 'com.raxdenstudios.android-application'
}
```

</details>

#### Android library

<details open><summary>Kotlin</summary>

```kt
plugins {
    id("android-library-conventions")
}
```

</details>

<details><summary>Groovy</summary>

```groovy
plugins {
    id 'com.raxdenstudios.android-library'
}
```

</details>

#### Android feature

<details open><summary>Kotlin</summary>

```kt
plugins {
    id("com.raxdenstudios.android-feature")
}
```

</details>

<details><summary>Groovy</summary>

```groovy
plugins {
    id 'com.raxdenstudios.android-feature'
}
```

</details>

### For compose

#### Android compose application

<details open><summary>Kotlin</summary>

```kt
plugins {
    id("com.raxdenstudios.android-compose-application")
}
```

</details>

<details><summary>Groovy</summary>

```groovy
plugins {
    id 'com.raxdenstudios.android-compose-application'
}
```

</details>

#### Android compose library

<details open><summary>Kotlin</summary>

```kt
plugins {
    id("com.raxdenstudios.android-compose-library")
}
```

</details>

<details><summary>Groovy</summary>

```groovy
plugins {
    id 'com.raxdenstudios.android-compose-library'
}
```

</details>

#### Android compose feature

<details open><summary>Kotlin</summary>

```kt
plugins {
    id("com.raxdenstudios.android-compose-feature")
}
```

</details>

<details><summary>Groovy</summary>

```groovy
plugins {
    id 'com.raxdenstudios.android-compose-feature'
}
```

</details>
