import com.raxdenstudios.publishing.model.Coordinates
import com.raxdenstudios.publishing.model.Developer

plugins {
    alias(libs.plugins.android.versioning)
    alias(libs.plugins.android.compose.library.conventions)
    alias(libs.plugins.android.publish.library)
}

android {
    namespace = "com.raxdenstudios.commons.android.compose"
}

versioning {
    filePath = "version.properties"
}

publishLibrary {
    name = "Android Compose"
    description = "Android compose library"
    url = "https://github.com/raxden/android-commons"
    developer = Developer(
        id = "raxden",
        name = "Ángel Gómez",
        email = "raxden.dev@gmail.com",
    )
    coordinates = Coordinates.default.copy(artifactId = "commons-android-compose")
}

dependencies {
    implementation(projects.libraries.core)

    api(platform(libs.androidx.compose.bom))
    api(libs.androidx.compose.runtime)
    api(libs.androidx.compose.ui)
    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.ui.tooling.preview)
    api(libs.androidx.compose.lifecycle.runtime)

    testImplementation(libs.bundles.test)
    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.compose.activity)
}
