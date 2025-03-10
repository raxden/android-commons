pluginManagement {
    includeBuild("build-logic")
    /**
     * The pluginManagement {repositories {...}} block configures the
     * repositories Gradle uses to search or download the Gradle plugins and
     * their transitive dependencies. Gradle pre-configures support for remote
     * repositories such as JCenter, Maven Central, and Ivy. You can also use
     * local repositories or define your own remote repositories. The code below
     * defines the Gradle Plugin Portal, Google's Maven repository,
     * and the Maven Central Repository as the repositories Gradle should use to look for its dependencies.
     */
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        mavenLocal()
    }
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") { from(files("./gradle/libraries.versions.toml")) }
    }
    /**
     * The dependencyResolutionManagement { repositories {...}}
     * block is where you configure the repositories and dependencies used by
     * all modules in your project, such as libraries that you are using to
     * create your application. However, you should configure module-specific
     * dependencies in each module-level build.gradle file. For new projects,
     * Android Studio includes Google's Maven repository and the
     * Maven Central Repository by
     * default, but it does not configure any dependencies (unless you select a
     * template that requires some).
     */
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        maven("https://jitpack.io")
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
    }

}

plugins {
    id("com.gradle.enterprise") version "3.15.1"
}

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"

        // Publishing a build scan for every build execution
        publishAlways()
    }
}

include(":libraries:core")
include(":libraries:android")
include(":libraries:android-binding")
include(":libraries:android-compose")
include(":libraries:android-test")
include(":libraries:coroutines")
include(":libraries:coroutines-test")
include(":libraries:network")
include(":libraries:network-rx")
include(":libraries:pagination")
include(":libraries:pagination-co")
include(":libraries:pagination-rx")
include(":libraries:permissions")
include(":libraries:preferences")
include(":libraries:rx")
include(":libraries:rx-test")
include(":libraries:threeten")
include(":libraries:threeten-test")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
