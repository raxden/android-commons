import com.raxdenstudios.publishing.model.Coordinates

plugins {
  id("com.raxdenstudios.android-versioning")
  id("com.android.library")
  id("kotlin-android")
  id("kotlin-kapt")
  id("com.raxdenstudios.publish-library")
}

versioning {
  group = "com.raxdenstudios"
}

publishLibrary {
  name = "Android"
  description = "Android library"
  url = "https://github.com/raxden/android-commons"
  developerId = "raxden"
  developerName = "Ángel Gómez"
  developerEmail = "raxden.dev@gmail.com"
  coordinates = Coordinates.default.copy(artifactId = "commons-android")
}

android {

  compileSdk = Versions.compileSdk

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }

  defaultConfig {
    minSdk = Versions.minSdk
    targetSdk = Versions.targetSdk

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    consumerProguardFile("consumer-rules.pro")
  }

  buildTypes {
    getByName("debug") {
      isMinifyEnabled = false
    }
  }
  buildFeatures {
    viewBinding = true
  }

  kotlinOptions {
    jvmTarget = "11"
  }
}

dependencies {
  implementation(AndroidLibraries.kotlinCore)
  implementation(AndroidLibraries.kotlinActivity)
  implementation(AndroidLibraries.kotlinFragment)
  implementation(AndroidLibraries.kotlinPreferences)
  implementation(AndroidLibraries.material)
  implementation(AndroidLibraries.playCore)
  implementation(AndroidLibraries.constraintLayout)
  implementation(AndroidLibraries.swipeRefreshLayout)
  implementation(AndroidLibraries.browser)
  implementation(AndroidLibraries.lifecycleExtensions)
  implementation(AndroidLibraries.lifecycleRuntime)
  implementation(AndroidLibraries.lifecycleCommon)
  implementation(AndroidLibraries.lifecycleViewModel)

  testImplementation(TestLibraries.atslJunit)
}
