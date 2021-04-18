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
  name = "Pagination"
  description = "Pagination library"
  url = "https://github.com/raxden/android-commons"
  developerId = "raxden"
  developerName = "Ángel Gómez"
  developerEmail = "raxden.dev@gmail.com"
  coordinates = Coordinates.default.copy(artifactId = "commons-pagination-rx")
}

android {
  buildTypes {
    getByName("debug") {
      isTestCoverageEnabled = true
    }
  }
}

dependencies {
  api(project(Modules.libraryPagination))
  api(project(Modules.libraryRx))

  testImplementation(project(Modules.libraryRxTest))
}
