plugins {
  id("com.raxdenstudios.android-library")
}

dependencies {
  implementation(platform(KotlinLibraries.coroutinesBom))
  api(TestLibraries.coroutinesTest)

  api(TestLibraries.archCoreTest)
  api(TestLibraries.atslJunit)
  api(TestLibraries.atslRules)
  api(TestLibraries.atslRunner)

  api(TestLibraries.mockkCore)
  api(TestLibraries.mockkAndroid)

  api(TestLibraries.koinTest)

  api(TestLibraries.threetenabp)

  api(TestLibraries.timberJunit)
}
