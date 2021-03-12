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
  name = "Rx Coroutines"
  description = "Coroutines commons is a library with a set of useful classes to help to developer to work with coroutines."
  url = "https://github.com/raxden/android-commons"
  developerId = "raxden"
  developerName = "Ángel Gómez"
  developerEmail = "raxden.dev@gmail.com"
  coordinates = Coordinates.default.copy(artifactId = "commons-coroutines")
}

android {
  buildTypes {
    getByName("debug") {
      isTestCoverageEnabled = true
    }
  }
}

dependencies {
  api(platform(KotlinLibraries.coroutinesBom))
  api(KotlinLibraries.coroutinesAndroid)
}