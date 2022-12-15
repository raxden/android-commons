import com.adarshr.gradle.testlogger.theme.ThemeType
import io.gitlab.arturbosch.detekt.extensions.DetektExtension

buildscript {
  repositories {
    google()
    jcenter()
    maven("https://plugins.gradle.org/m2/")
  }
  dependencies {
    classpath("com.raxdenstudios:android-plugins:0.41")
  }
}

plugins {
  id("com.vanniktech.android.junit.jacoco") version "0.16.0"
  id("io.codearte.nexus-staging") version "0.22.0"
  id("com.raxdenstudios.android-releasing") version "0.41"
  id("com.adarshr.test-logger") version "3.2.0"
  id("io.gitlab.arturbosch.detekt") version "1.15.0"
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

allprojects {
  repositories {
    google()
    jcenter()
    maven("https://jitpack.io")
  }
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
    toolVersion = "1.15.0"
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
