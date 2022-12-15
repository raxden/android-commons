import com.adarshr.gradle.testlogger.theme.ThemeType
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

buildscript {
    repositories {
        google()
        maven("https://plugins.gradle.org/m2/")
    }
    dependencies {
        classpath("com.android.tools.build:gradle:${Versions.androidGradlePlugin}")
        classpath("com.raxdenstudios:android-plugins:${Versions.androidPlugins}")
    }
}

plugins {
    id("org.jetbrains.kotlin.android") version Versions.kotlin apply false

    id("com.vanniktech.android.junit.jacoco") version Versions.jacocoPlugin
    id("io.codearte.nexus-staging") version Versions.nexusStagingPlugin
    id("com.raxdenstudios.android-releasing") version Versions.androidPlugins
    id("com.adarshr.test-logger") version Versions.testLoggerPlugin
    id("io.gitlab.arturbosch.detekt") version Versions.detektPlugin
    id("com.github.ben-manes.versions") version Versions.benNamesPlugin
}

val nexusId: String? by project
val nexusUsername: String? by project
val nexusPassword: String? by project

fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}

tasks.named<DependencyUpdatesTask>("dependencyUpdates").configure {
    outputFormatter = "html"
    rejectVersionIf {
        isNonStable(candidate.version) && !isNonStable(currentVersion)
    }
}

nexusStaging {
    packageGroup = "com.raxdenstudios"
    stagingProfileId = nexusId ?: System.getenv("OSSRH_ID") ?: ""
    username = nexusUsername ?: System.getenv("OSSRH_USERNAME") ?: ""
    password = nexusPassword ?: System.getenv("OSSRH_PASSWORD") ?: ""
}

detekt {
    // version found will be used. Override to stay on the same version.
    toolVersion = Versions.detektPlugin
    config = files("/config/detekt/detekt.yml")
    // Builds the AST in parallel. Rules are always executed in parallel. Can lead to speedups in larger projects.
    parallel = true
    // Specify the base path for file paths in the formatted reports.
    basePath = "${rootProject.projectDir}"
}

subprojects {
    apply(plugin = "com.adarshr.test-logger")
    apply(plugin = "io.gitlab.arturbosch.detekt")

    testlogger {
        theme = ThemeType.MOCHA
        slowThreshold = 3000
    }

    dependencies {
        // This rule set provides wrappers for rules implemented by ktlint - https://ktlint.github.io/.
        // https://detekt.dev/docs/rules/formatting/
        detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:${Versions.detektPlugin}")
    }
}

tasks {
    val clean by registering(Delete::class) {
        delete(buildDir)
    }
}
