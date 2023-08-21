import com.raxdenstudios.publishing.model.Coordinates

plugins {
    alias(libs.plugins.android.versioning)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.android.publish.library)
}

versioning {
    filePath = "./libraries/core/version.properties"
}

publishLibrary {
    name = "Android"
    description = "Android Core"
    url = "https://github.com/raxden/android-commons"
    developerId = "raxden"
    developerName = "Ángel Gómez"
    developerEmail = "raxden.dev@gmail.com"
    coordinates = Coordinates.default.copy(artifactId = "commons-core")
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
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
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
//    implementation(libs.androidx.core.ktx)
    testImplementation(libs.junit.ktx)
    testImplementation(libs.truth)
    testImplementation(libs.bundles.mockk)
}
