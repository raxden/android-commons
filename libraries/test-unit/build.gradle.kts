import com.raxdenstudios.publishing.model.Coordinates

plugins {
  id("com.raxdenstudios.android-versioning")
  id("com.raxdenstudios.android-library")
  id("com.raxdenstudios.publish-library")
  id("com.raxdenstudios.android-jacoco")
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
  coordinates = Coordinates.default.copy(artifactId = "commons-test-unit")
}

dependencies {
  implementation(platform(KotlinLibraries.coroutinesBom))
  implementation(TestLibraries.coroutinesTest)

  implementation(TestLibraries.archCoreTest)
  implementation(TestLibraries.atslJunit)
  implementation(TestLibraries.atslRules)
  implementation(TestLibraries.atslRunner)

  implementation(TestLibraries.mockkCore)
  implementation(TestLibraries.mockkAndroid)

  implementation(TestLibraries.koinTest)

  implementation(TestLibraries.threetenabp)

  implementation(TestLibraries.timberJunit)
}
