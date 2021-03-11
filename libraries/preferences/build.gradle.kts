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
  name = "AdvancedPreferences"
  description = "Improve your shared preferences with the power of Gson"
  url = "https://github.com/raxden/android-commons"
  developerId = "raxden"
  developerName = "Ángel Gómez"
  developerEmail = "raxden.dev@gmail.com"
  coordinates = Coordinates.default.copy(artifactId = "commons-preferences")
}

android {
  buildTypes {
    getByName("debug") {
      isTestCoverageEnabled = true
    }
  }
  buildFeatures {
    viewBinding = true
  }
}

dependencies {
  api(AndroidLibraries.kotlinPreferences)
  api(Libraries.gson)

  testImplementation(TestLibraries.atslJunit)
  testImplementation(TestLibraries.robolectric)
}
