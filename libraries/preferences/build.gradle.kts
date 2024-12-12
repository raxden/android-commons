import com.raxdenstudios.publishing.model.Coordinates

plugins {
    alias(libs.plugins.android.versioning)
    id("android-library-conventions")
    alias(libs.plugins.android.publish.library)
}

android {
    namespace = "com.raxdenstudios.commons.preferences"
}

versioning {
    filePath = "./libraries/preferences/version.properties"
}

publishLibrary {
    name = "Preferences Commons"
    description = "Preferences commons is a library with a set of useful classes to help to " +
            "developer to work with preferences."
    url = "https://github.com/raxden/android-commons"
    developerId = "raxden"
    developerName = "Ángel Gómez"
    developerEmail = "raxden.dev@gmail.com"
    coordinates = Coordinates.default.copy(artifactId = "commons-preferences")
}

dependencies {
    api(libs.androidx.preference.ktx)
    api(libs.gson)

    testImplementation(libs.androidx.test.core)
    testImplementation(libs.androidx.test.junit.ktx)
    testImplementation(libs.robolectric)
}
