import com.adarshr.gradle.testlogger.theme.ThemeType

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.junit.jacoco)
    alias(libs.plugins.nexus.publish)
    alias(libs.plugins.android.releasing)
    alias(libs.plugins.test.logger)
    alias(libs.plugins.detekt)
}

val nexusId: String? by project
val nexusUsername: String? by project
val nexusPassword: String? by project

releasing {
    // ./gradlew releaseCandidate --no-configuration-cache
    // ./gradlew releaseCandidateTag --no-configuration-cache
    versionFilePath = "./version.properties"
}

nexusPublishing {
    repositories {
        sonatype {
            packageGroup.set("com.raxdenstudios")
            // stagingProfileId can reduce execution time by even 10 seconds
            stagingProfileId.set(nexusId ?: System.getenv("OSSRH_ID") ?: "")
            username.set(nexusUsername ?: System.getenv("OSSRH_USERNAME") ?: "")
            password.set(nexusPassword ?: System.getenv("OSSRH_PASSWORD") ?: "")
        }
    }
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

    testlogger {
        theme = ThemeType.MOCHA
        slowThreshold = 3000
    }

    dependencies {
        // This rule set provides wrappers for rules implemented by ktlint - https://ktlint.github.io/.
        // https://detekt.dev/docs/rules/formatting/
        detektPlugins(rootProject.libs.plugins.detekt.formatting.get().toString())
    }
}

tasks {
    val clean by registering(Delete::class) {
        delete(buildDir)
    }
}
