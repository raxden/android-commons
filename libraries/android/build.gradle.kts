import com.raxdenstudios.publishing.model.Coordinates

plugins {
    alias(libs.plugins.android.versioning)
    id("com.raxdenstudios.android-library")
    alias(libs.plugins.android.publish.library)
}

versioning {
    filePath = "./libraries/android/version.properties"
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

dependencies {
    api(project(":libraries:core"))

    implementation(libs.bundles.material)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.swiperefreshlayout)

    testImplementation(libs.bundles.test)
}
