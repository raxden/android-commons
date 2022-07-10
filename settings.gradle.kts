import org.gradle.api.initialization.resolve.RepositoriesMode

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}

include(":libraries:android")
include(":libraries:android-test")
include(":libraries:coroutines")
include(":libraries:coroutines-test")
include(":libraries:glide")
include(":libraries:okhttp3")
include(":libraries:pagination")
include(":libraries:pagination-co")
include(":libraries:pagination-rx")
include(":libraries:permissions")
include(":libraries:preferences")
include(":libraries:retrofit")
include(":libraries:retrofit-co")
include(":libraries:retrofit-rx")
include(":libraries:rx")
include(":libraries:rx-test")
include(":libraries:threeten")
