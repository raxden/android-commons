import com.raxdenstudios.publishing.model.Coordinates
import com.raxdenstudios.publishing.model.Developer

plugins {
    alias(libs.plugins.android.versioning)
    alias(libs.plugins.android.library.conventions)
    alias(libs.plugins.android.publish.library)
}

android {
    namespace = "com.raxdenstudios.commons.preferences"
}

versioning {
    filePath = "version.properties"
}

publishLibrary {
    name = "Preferences Commons"
    description = "Preferences commons is a library with a set of useful classes to help to " +
            "developer to work with preferences."
    url = "https://github.com/raxden/android-commons"
    developer = Developer(
        id = "raxden",
        name = "Ángel Gómez",
        email = "raxden.dev@gmail.com",
    )
    coordinates = Coordinates.default.copy(artifactId = "commons-preferences")
}

dependencies {
    api(libs.androidx.preference.ktx)
    api(libs.gson)

    testImplementation(libs.androidx.arch.core.testing)
    testImplementation(libs.androidx.test.junit.ktx)
    testImplementation(libs.robolectric)
}
