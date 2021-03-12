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
  api(TestLibraries.archCoreTest)
  api(TestLibraries.atslJunit)
  api(TestLibraries.atslRules)
  api(TestLibraries.atslRunner)

  api(TestLibraries.mockkCore)
  api(TestLibraries.mockkAndroid)

  api(TestLibraries.koinTest)

  api(TestLibraries.restMock)

  api(TestLibraries.timberJunit)

  api(TestAndroidLibraries.espresso)
  api(TestAndroidLibraries.espressoContrib)
  api(TestAndroidLibraries.espressoWebView)
  api(TestAndroidLibraries.espressoIntents)
}
