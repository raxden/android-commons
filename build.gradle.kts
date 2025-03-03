import com.adarshr.gradle.testlogger.theme.ThemeType

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.versioning) apply false
    alias(libs.plugins.android.publish.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.nexus.publish)
    alias(libs.plugins.test.logger)
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

subprojects {
    apply(plugin = "com.adarshr.test-logger")

    group = "com.raxdenstudios"

    testlogger {
        theme = ThemeType.MOCHA
        slowThreshold = 3000
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
