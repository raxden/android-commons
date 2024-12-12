import com.adarshr.gradle.testlogger.theme.ThemeType

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.versioning) apply false
    alias(libs.plugins.android.publish.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.rootcoverage)
    alias(libs.plugins.nexus.publish)
    alias(libs.plugins.test.logger)
    alias(libs.plugins.detekt)
    id("android-project-conventions")
}

val nexusId: String? by project
val nexusUsername: String? by project
val nexusPassword: String? by project

nexusPublishing {
    this.repositories {
        sonatype {
            packageGroup.set("com.raxdenstudios")
            repositoryDescription.set("Raxden Studios Maven2 Repository")

            // stagingProfileId can reduce execution time by even 10 seconds
            stagingProfileId.set(nexusId ?: System.getenv("OSSRH_ID") ?: "")
            username.set(nexusUsername ?: System.getenv("OSSRH_USERNAME") ?: "")
            password.set(nexusPassword ?: System.getenv("OSSRH_PASSWORD") ?: "")
        }
    }
}

rootCoverage {
    // The default build variant for every module
    buildVariant = "debug"

    // Class & package exclude patterns
    excludes = listOf(
        "**/di/*",
        "**/BuildConfig*",
        "**/databinding/*",
        "**/*_*.class",
        "**/*_Impl*.class",
        "**/App.class",
        "**/*Activity.*",
        "**/*Fragment.*",
        "**/*Adapter.*",
        "**/*.compose.*",
    )

    // Since 1.1 generateHtml is by default true
    generateCsv = false
    generateHtml = true
    generateXml = true

    // Since 1.2: Same as executeTests except that this only affects the instrumented Android tests
    executeAndroidTests = false

    // Since 1.2: Same as executeTests except that this only affects the unit tests
    executeUnitTests = false

    // Since 1.2: When true include results from instrumented Android tests into the coverage report
    includeAndroidTestResults = true

    // Since 1.2: When true include results from unit tests into the coverage report
    includeUnitTestResults = true

    // Since 1.4: Sets jacoco.includeNoLocationClasses, so you don't have to. Helpful when using Robolectric
    // which usually requires this attribute to be true
    includeNoLocationClasses = true
}

detekt {
    // version found will be used. Override to stay on the same version.
    config = files("/config/detekt/detekt.yml")
    // Builds the AST in parallel. Rules are always executed in parallel. Can lead to speedups in larger projects.
    parallel = true
    // Specify the base path for file paths in the formatted reports.
    basePath = rootProject.projectDir.toString()
}

subprojects {
    apply(plugin = "com.adarshr.test-logger")
    apply(plugin = "io.gitlab.arturbosch.detekt")
    // https://docs.gradle.org/current/userguide/project_report_plugin.html#sec:project_reports_tasks
    apply(plugin = "project-report")

    group = "com.raxdenstudios"

    testlogger {
        theme = ThemeType.MOCHA
        slowThreshold = 3000
    }

    dependencies {
        // This rule set provides wrappers for rules implemented by ktlint - https://ktlint.github.io/.
        // https://detekt.dev/docs/rules/formatting/dawd
        detektPlugins(rootProject.libs.plugins.detekt.formatting.get().toString())
    }

    configurations.all {
        exclude(group = "com.raxdenstudios", module = "commons-android")
        exclude(group = "com.raxdenstudios", module = "commons-android-binding")
        exclude(group = "com.raxdenstudios", module = "commons-android-compose")
        exclude(group = "com.raxdenstudios", module = "commons-android-test")
        exclude(group = "com.raxdenstudios", module = "commons-core")
        exclude(group = "com.raxdenstudios", module = "commons-coroutines")
        exclude(group = "com.raxdenstudios", module = "commons-coroutines-test")
        exclude(group = "com.raxdenstudios", module = "commons-network")
        exclude(group = "com.raxdenstudios", module = "commons-network-rx")
        exclude(group = "com.raxdenstudios", module = "commons-pagination")
        exclude(group = "com.raxdenstudios", module = "commons-pagination-co")
        exclude(group = "com.raxdenstudios", module = "commons-pagination-rx")
        exclude(group = "com.raxdenstudios", module = "commons-permissions")
        exclude(group = "com.raxdenstudios", module = "commons-preferences")
        exclude(group = "com.raxdenstudios", module = "commons-rx")
        exclude(group = "com.raxdenstudios", module = "commons-rx-test")
        exclude(group = "com.raxdenstudios", module = "commons-threeten")
        exclude(group = "com.raxdenstudios", module = "commons-threeten-test")
    }
}

tasks {
    val clean by registering(Delete::class) {
        delete(buildDir)
    }
}
