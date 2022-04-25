plugins {
  `kotlin-dsl`
}
// Required since Gradle 4.10+.
repositories {
  mavenCentral()
  google()
}

dependencies {
  implementation("com.android.tools.build:gradle:7.1.3")
  implementation("org.ajoberstar.grgit:grgit-gradle:5.0.0")
  implementation(gradleApi())
  implementation(localGroovy())
}

gradlePlugin {
  plugins {
    create("releasingPlugin") {
      id = "com.raxdenstudios.androidd-releasing"
      implementationClass = "com.raxdenstudios.releasing.ReleasingPlugin"
    }
  }
}
