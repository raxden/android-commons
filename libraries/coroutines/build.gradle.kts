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
  name = "Coroutines"
  description = "Coroutines library"
  url = "https://github.com/raxden/android-commons"
  developerId = "raxden"
  developerName = "Ángel Gómez"
  developerEmail = "raxden.dev@gmail.com"
  coordinates = Coordinates.default.copy(artifactId = "commons-coroutines")
}

dependencies {
  api(platform(KotlinLibraries.coroutinesBom))
  api(KotlinLibraries.coroutinesAndroid)

  testImplementation(TestLibraries.atslJunit)
}
