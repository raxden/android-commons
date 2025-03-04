import com.raxdenstudios.publishing.model.Coordinates

plugins {
    alias(libs.plugins.android.versioning)
    alias(libs.plugins.android.library.conventions)
    alias(libs.plugins.android.publish.library)
}

android {
    namespace = "com.raxdenstudios.commons.android.test"
}

versioning {
    filePath = "./libraries/$name/version.properties"
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
