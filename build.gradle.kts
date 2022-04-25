import com.adarshr.gradle.testlogger.theme.ThemeType
import io.gitlab.arturbosch.detekt.extensions.DetektExtension

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
//  id("com.android.application") version Versions.androidGradlePlugin apply false
//  id("com.android.library") version Versions.androidGradlePlugin apply false
  id("org.jetbrains.kotlin.android") version Versions.kotlin apply false

  id("com.vanniktech.android.junit.jacoco") version Versions.jacocoPlugin
  id("io.codearte.nexus-staging") version Versions.nexusStagingPlugin
  id("com.raxdenstudios.androidd-releasing")
  id("com.adarshr.test-logger") version Versions.testLoggerPlugin
  id("io.gitlab.arturbosch.detekt") version Versions.detektPlugin
}

junitJacoco {

}

val nexusId: String? by project
val nexusUsername: String? by project
val nexusPassword: String? by project

nexusStaging {
  packageGroup = "com.raxdenstudios"
  stagingProfileId = nexusId ?: System.getenv("OSSRH_ID") ?: ""
  username = nexusUsername ?: System.getenv("OSSRH_USERNAME") ?: ""
  password = nexusPassword ?: System.getenv("OSSRH_PASSWORD") ?: ""
}

subprojects {
  apply(plugin = "com.adarshr.test-logger")
  apply(plugin = "io.gitlab.arturbosch.detekt")

  testlogger {
    theme = ThemeType.MOCHA
    slowThreshold = 3000
  }

  configure<DetektExtension> {
    // To create detekt.yml -> gradle detektGenerateConfig
    toolVersion = Versions.detektPlugin
    config = files("${project.rootDir}/config/detekt/detekt.yml")
    buildUponDefaultConfig = true
    reports.html.enabled = true
  }
}

tasks {
  val clean by registering(Delete::class) {
    delete(buildDir)
  }
}
