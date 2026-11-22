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

    api(libs.androidx.test.junit.ktx)
    api(libs.bundles.test.espresso)
    api(libs.androidx.test.runner)
    api(libs.okhttp3)

    implementation(libs.bundles.test)
    implementation(libs.bundles.test.mockwebserver)
}
