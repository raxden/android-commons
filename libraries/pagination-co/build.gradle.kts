import com.raxdenstudios.publishing.model.Coordinates

plugins {
    alias(libs.plugins.android.versioning)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.android.publish.library)
}

versioning {
    filePath = "./libraries/pagination-co/version.properties"
}

publishLibrary {
    name = "Pagination"
    description = "Pagination library"
    url = "https://github.com/raxden/android-commons"
    developerId = "raxden"
    developerName = "Ángel Gómez"
    developerEmail = "raxden.dev@gmail.com"
    coordinates = Coordinates.default.copy(artifactId = "commons-pagination-co")
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

    kotlinOptions {
        jvmTarget = Versions.jvmTarget
    }
}

dependencies {
    api(project(Modules.libraryPagination))
    api(project(Modules.libraryCoroutines))

    implementation(platform(libs.coroutines.bom))
    implementation(libs.coroutines.android)

    testImplementation(platform(libs.coroutines.bom))
    testImplementation(libs.coroutines.test)
    testImplementation(libs.junit.ktx)
    testImplementation(libs.bundles.mockk)
}
