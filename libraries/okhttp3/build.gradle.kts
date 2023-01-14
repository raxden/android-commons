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
    name = "Okhttp3"
    description = "Okhttp3 library"
    url = "https://github.com/raxden/android-commons"
    developerId = "raxden"
    developerName = "Ángel Gómez"
    developerEmail = "raxden.dev@gmail.com"
    coordinates = Coordinates.default.copy(artifactId = "commons-okhttp3")
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
    implementation(platform(libs.okhttp3.bom))
    implementation(libs.bundles.okhttp3)

    testImplementation(libs.junit.ktx)
}
