
import com.raxdenstudios.publishing.model.Coordinates
import com.raxdenstudios.publishing.model.Developer

plugins {
    alias(libs.plugins.android.versioning)
    alias(libs.plugins.android.library.conventions)
    alias(libs.plugins.android.publish.library)
}

android {
    namespace = "com.raxdenstudios.commons.android"
}

versioning {
    filePath = "version.properties"
}

publishLibrary {
    name = "Android"
    description = "Android library"
    url = "https://github.com/raxden/android-commons"
    developer = Developer(
        id = "raxden",
        name = "Ángel Gómez",
        email = "raxden.dev@gmail.com",
    )
    coordinates = Coordinates.default.copy(artifactId = "commons-android")
}

dependencies {
    api(projects.libraries.core)

    api(libs.android.material)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.swiperefreshlayout)

    testImplementation(libs.bundles.test)
}
