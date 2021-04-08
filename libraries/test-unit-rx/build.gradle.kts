plugins {
  id("com.raxdenstudios.android-library")
}

dependencies {
  api(project(Modules.libraryTest))

  api(Libraries.rxAndroid)
  api(Libraries.rxKotlin)
}
