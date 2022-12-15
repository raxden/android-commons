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
        sourceCompatibility = Versions.sourceCompatibility
        targetCompatibility = Versions.targetCompatibility
    }

    defaultConfig {
        minSdk = Versions.minSdk
        targetSdk = Versions.targetSdk

        testInstrumentationRunner = Versions.testInstrumentationRunner
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
        jvmTarget = Versions.jvmTarget
    }
}

dependencies {
    api(AndroidLibraries.kotlinCore)
    api(AndroidLibraries.kotlinActivity)
    api(AndroidLibraries.kotlinFragment)
    api(AndroidLibraries.kotlinPreferences)
    api(AndroidLibraries.material)
    api(AndroidLibraries.playCore)
    api(AndroidLibraries.constraintLayout)
    api(AndroidLibraries.swipeRefreshLayout)
    api(AndroidLibraries.browser)
    api(AndroidLibraries.lifecycleRuntime)

    testImplementation(TestLibraries.atslJunit)
}
