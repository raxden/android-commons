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
  name = "Permissions Commons"
  description = "Permissions commons is a library with a set of useful classes to help to developer to work with permissions."
  url = "https://github.com/raxden/android-commons"
  developerId = "raxden"
  developerName = "Ángel Gómez"
  developerEmail = "raxden.dev@gmail.com"
  coordinates = Coordinates.default.copy(artifactId = "commons-permissions")
}

android {

  compileSdk = Versions.compileSdk

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
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

  kotlinOptions {
    jvmTarget = "1.8"
  }
}

dependencies {
  implementation(project(Modules.libraryAndroid))
}
