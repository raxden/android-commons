import com.raxdenstudios.publishing.model.Coordinates
import com.raxdenstudios.publishing.model.Developer

plugins {
    alias(libs.plugins.android.versioning)
    alias(libs.plugins.android.library.conventions)
    alias(libs.plugins.android.publish.library)
}

android {
    namespace = "com.raxdenstudios.commons.core"
}

versioning {
    filePath = "version.properties"
}

publishLibrary {
    name = "Android"
    description = "Android Core"
    url = "https://github.com/raxden/android-commons"
    developer = Developer(
        id = "raxden",
        name = "Ángel Gómez",
        email = "raxden.dev@gmail.com",
    )
    coordinates = Coordinates.default.copy(artifactId = "commons-core")
}

dependencies {
    api(libs.timber)

    testImplementation(libs.androidx.test.core)
    testImplementation(libs.truth)
    testImplementation(libs.mockk.android)
}
