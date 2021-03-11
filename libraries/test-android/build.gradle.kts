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
  name = "AndroidTest Commons"
  description = "AndroidTest commons is a library with a set of useful classes to help to developer to work with android testing."
  url = "https://github.com/raxden/android-commons"
  developerId = "raxden"
  developerName = "Ángel Gómez"
  developerEmail = "raxden.dev@gmail.com"
  coordinates = Coordinates.default.copy(artifactId = "commons-android-test")
}

dependencies {
  implementation(TestLibraries.archCoreTest)
  implementation(TestLibraries.atslJunit)
  implementation(TestLibraries.atslRules)
  implementation(TestLibraries.atslRunner)

  implementation(TestLibraries.mockkCore)
  implementation(TestLibraries.mockkAndroid)

  implementation(TestLibraries.koinTest)

  implementation(TestLibraries.restMock)

  implementation(TestLibraries.timberJunit)

  implementation(TestAndroidLibraries.espresso)
  implementation(TestAndroidLibraries.espressoContrib)
  implementation(TestAndroidLibraries.espressoWebView)
  implementation(TestAndroidLibraries.espressoIntents)
}
