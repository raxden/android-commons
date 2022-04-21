import com.raxdenstudios.publishing.model.Coordinates
import com.adarshr.gradle.testlogger.theme.ThemeType

plugins {
  id("com.raxdenstudios.android-versioning")
  id("com.raxdenstudios.android-library")
  id("com.raxdenstudios.publish-library")
  id("com.adarshr.test-logger")
}

versioning {
  group = "com.raxdenstudios"
}

publishLibrary {
  name = "Permissions Commons"
  description = "Permissions commons is a library with a set of useful classes to help to developer to work with permissions."
  url = "https://github.com/raxden/android-commons"
  developerId = "raxden"
  developerName = "Ángel Gómez"
  developerEmail = "raxden.dev@gmail.com"
  coordinates = Coordinates.default.copy(artifactId = "commons-permissions")
}

dependencies {
  api(project(Modules.libraryAndroid))
}
