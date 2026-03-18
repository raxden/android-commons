import com.raxdenstudios.publishing.model.Coordinates
import com.raxdenstudios.publishing.model.Developer

plugins {
    alias(libs.plugins.android.versioning)
    alias(libs.plugins.android.library.conventions)
    alias(libs.plugins.android.publish.library)
}

android {
    namespace = "com.raxdenstudios.commons.android.test"
}

versioning {
    filePath = "version.properties"
}

publishLibrary {
    name = "Android test"
    description = "Android Test library"
    url = "https://github.com/raxden/android-commons"
    developer = Developer(
        id = "raxden",
        name = "Ángel Gómez",
        email = "raxden.dev@gmail.com",
    )
    coordinates = Coordinates.default.copy(artifactId = "commons-android-test")
}

dependencies {
    implementation(libs.androidx.swiperefreshlayout)

    implementation(libs.bundles.test)
    implementation(libs.bundles.test.espresso)
    implementation(libs.bundles.test.mockwebserver)
}
