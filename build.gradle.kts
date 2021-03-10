buildscript {
  repositories {
    google()
    jcenter()
    maven("https://plugins.gradle.org/m2/")
  }
  dependencies {
    classpath("com.raxdenstudios:android-plugins:0.37")
  }
}

plugins {
  id("project-report")
  id("io.codearte.nexus-staging").version("0.22.0")
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

tasks {
  val clean by registering(Delete::class) {
    delete(buildDir)
  }
}
