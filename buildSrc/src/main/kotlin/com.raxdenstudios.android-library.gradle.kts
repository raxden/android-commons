plugins {
  id("com.android.library")
  id("com.raxdenstudios.android-detekt")
  id("kotlin-android")
  id("kotlin-kapt")
}

android {

  compileSdkVersion(Versions.compileSdk)

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }

  defaultConfig {
    minSdkVersion(Versions.minSdk)
    targetSdkVersion(Versions.targetSdk)

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    consumerProguardFile("consumer-rules.pro")
  }

  buildTypes {
    getByName("debug") {
      isMinifyEnabled = false
    }
  }

  kotlinOptions {
    jvmTarget = "1.8"
  }
}
