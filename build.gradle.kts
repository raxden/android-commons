import com.adarshr.gradle.testlogger.theme.ThemeType

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

  testlogger {
    theme = ThemeType.MOCHA
    slowThreshold = 3000
  }
}

tasks {
  val clean by registering(Delete::class) {
    delete(buildDir)
  }
}
