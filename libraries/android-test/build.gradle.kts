import com.raxdenstudios.publishing.model.Coordinates

plugins {
    alias(libs.plugins.android.versioning)
    id("android-library-conventions")
    alias(libs.plugins.android.publish.library)
}

versioning {
    filePath = "./libraries/android-test/version.properties"
}

publishLibrary {
    name = "Android test"
    description = "Android Test library"
    url = "https://github.com/raxden/android-commons"
    developerId = "raxden"
    developerName = "Ángel Gómez"
    developerEmail = "raxden.dev@gmail.com"
    coordinates = Coordinates.default.copy(artifactId = "commons-android-test")
}

dependencies {
    implementation(libs.androidx.swiperefreshlayout)

    implementation(libs.bundles.test)
    implementation(libs.bundles.test.espresso)
    implementation(libs.bundles.test.mockwebserver)
}
