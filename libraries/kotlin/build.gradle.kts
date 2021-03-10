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
  name = "AndroidCommons"
  description = "Android commons"
  url = "https://github.com/raxden/android-commons"
  developerId = "raxden"
  developerName = "Ángel Gómez"
  developerEmail = "raxden.dev@gmail.com"
}

dependencies {
  api(KotlinLibraries.kotlinStdlib)
  api(KotlinLibraries.kotlinReflect)

  testImplementation(project(Modules.libraryTest))
}

