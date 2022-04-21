import com.raxdenstudios.publishing.model.Coordinates
import com.adarshr.gradle.testlogger.theme.ThemeType

plugins {
  id("com.raxdenstudios.android-versioning")
  id("com.raxdenstudios.android-library")
  id("com.raxdenstudios.publish-library")
  id("com.adarshr.test-logger")
}

testlogger {
  theme = ThemeType.MOCHA
  slowThreshold = 3000
}

versioning {
  group = "com.raxdenstudios"
}

publishLibrary {
  name = "Retrofit Commons"
  description = "Retrofit commons is a library with a set of useful classes to help to developer to work with retrofit."
  url = "https://github.com/raxden/android-commons"
  developerId = "raxden"
  developerName = "Ángel Gómez"
  developerEmail = "raxden.dev@gmail.com"
  coordinates = Coordinates.default.copy(artifactId = "commons-retrofit-co")
}

dependencies {
  api(project(Modules.libraryRetrofit))
  api(project(Modules.libraryCoroutines))

  api(Libraries.retrofitNetworkResponseAdapter)

  testImplementation(TestLibraries.atslJunit)
}
