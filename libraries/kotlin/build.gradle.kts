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
  name = "Kotlin Commons"
  description = "Kotlin commons is a library with a set of useful classes to help to developer to work with kotlin."
  url = "https://github.com/raxden/android-commons"
  developerId = "raxden"
  developerName = "Ángel Gómez"
  developerEmail = "raxden.dev@gmail.com"
  coordinates = Coordinates.default.copy(artifactId = "commons-kotlin")
}

dependencies {
  api(KotlinLibraries.kotlinStdlib)
  api(KotlinLibraries.kotlinReflect)

  testImplementation(TestLibraries.atslJunit)
}

