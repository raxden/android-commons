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
  name = "Coroutines"
  description = "Coroutines library"
  url = "https://github.com/raxden/android-commons"
  developerId = "raxden"
  developerName = "Ángel Gómez"
  developerEmail = "raxden.dev@gmail.com"
  coordinates = Coordinates.default.copy(artifactId = "commons-coroutines")
}

dependencies {
  api(project(Modules.libraryBase))

  api(platform(KotlinLibraries.coroutinesBom))
  api(KotlinLibraries.coroutinesAndroid)
}
