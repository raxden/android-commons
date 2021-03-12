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
  name = "TestUnit Commons"
  description = "TestUnit commons is a library with a set of useful classes to help to developer to work with unit testing."
  url = "https://github.com/raxden/android-commons"
  developerId = "raxden"
  developerName = "Ángel Gómez"
  developerEmail = "raxden.dev@gmail.com"
  coordinates = Coordinates.default.copy(artifactId = "commons-unit-test")
}

dependencies {
  api(platform(KotlinLibraries.coroutinesBom))
  api(TestLibraries.coroutinesTest)

  api(Libraries.rxAndroid)
  api(Libraries.rxKotlin)

  api(TestLibraries.archCoreTest)
  api(TestLibraries.atslJunit)
  api(TestLibraries.atslRules)
  api(TestLibraries.atslRunner)

  api(TestLibraries.mockkCore)
  api(TestLibraries.mockkAndroid)

  api(TestLibraries.koinTest)

  api(TestLibraries.threetenabp)

  api(TestLibraries.timberJunit)
}
