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
    implementation(libs.androidx.preference.ktx)
    implementation(libs.gson)

    testImplementation(libs.bundles.test)
    testImplementation(libs.robolectric)
}
