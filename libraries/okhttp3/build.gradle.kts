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
  name = "OkHttp3 Commons"
  description = "OkHttp3 commons is a library with a set of useful classes to help to developer to work with okhttp3."
  url = "https://github.com/raxden/android-commons"
  developerId = "raxden"
  developerName = "Ángel Gómez"
  developerEmail = "raxden.dev@gmail.com"
  coordinates = Coordinates.default.copy(artifactId = "commons-okhttp3")
}

dependencies {
  api(platform(Libraries.okHttpBom))
  api(Libraries.okHttp)
  api(Libraries.okHttpLoggingInterceptor)
  
  implementation(Libraries.timber)
}
