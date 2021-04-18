import com.raxdenstudios.publishing.model.Coordinates

plugins {
  id("com.raxdenstudios.android-versioning")
  id("com.raxdenstudios.android-library")
  id("com.raxdenstudios.publish-library")
}

versioning {
  group = "com.raxdenstudios"
}

publishLibrary {
  name = "Preferences Commons"
  description = "Preferences commons is a library with a set of useful classes to help to developer to work with preferences."
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
}

dependencies {
  api(project(Modules.libraryBase))

  api(AndroidLibraries.kotlinPreferences)
  api(Libraries.gson)

  testImplementation(TestLibraries.atslJunit)
  testImplementation(TestLibraries.robolectric)
}
