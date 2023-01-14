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
    name = "Rx Test Commons"
    description =
        "Rx Test commons is a library with a set of useful classes to help to developer to work with Rx."
    url = "https://github.com/raxden/android-commons"
    developerId = "raxden"
    developerName = "Ángel Gómez"
    developerEmail = "raxden.dev@gmail.com"
    coordinates = Coordinates.default.copy(artifactId = "commons-rx-test")
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

    kotlinOptions {
        jvmTarget = Versions.jvmTarget
    }
}

dependencies {
    implementation(libs.junit.ktx)
    implementation(libs.bundles.rx)
}
