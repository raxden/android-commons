import com.raxdenstudios.publishing.model.Coordinates

plugins {
  id("com.raxdenstudios.android-versioning")
  id("com.raxdenstudios.android-library")
  id("com.raxdenstudios.publish-library")
}

versioning {
  versionFilePath = "./config/version.properties"
  group = "com.raxdenstudios"
}

publishLibrary {
  name = "Base Commons"
  description = "Base commons contains the clases used by anothers commons libraries."
  url = "https://github.com/raxden/android-commons"
  developerId = "raxden"
  developerName = "Ángel Gómez"
  developerEmail = "raxden.dev@gmail.com"
  coordinates = Coordinates.default.copy(artifactId = "commons-core")
}

android {
  buildTypes {
    getByName("debug") {
      isTestCoverageEnabled = true
    }
  }
}

dependencies {
  implementation(KotlinLibraries.kotlinStdlib)
  implementation(KotlinLibraries.kotlinReflect)

  implementation(AndroidLibraries.kotlinCore)
  implementation(AndroidLibraries.kotlinActivity)
  implementation(AndroidLibraries.kotlinFragment)
  implementation(AndroidLibraries.kotlinPreferences)
  implementation(AndroidLibraries.material)
  implementation(AndroidLibraries.playCore)
  implementation(AndroidLibraries.constraintLayout)
  implementation(AndroidLibraries.swipeRefreshLayout)
  implementation(AndroidLibraries.browser)
  implementation(AndroidLibraries.lifecycleExtensions)
  implementation(AndroidLibraries.lifecycleRuntime)
  implementation(AndroidLibraries.lifecycleCommon)
  implementation(AndroidLibraries.lifecycleViewModel)

  testImplementation(TestLibraries.atslJunit)
}
