import com.raxdenstudios.publishing.model.Coordinates

plugins {
    alias(libs.plugins.android.versioning)
    alias(libs.plugins.android.library.conventions)
    alias(libs.plugins.android.publish.library)
}

android {
    namespace = "com.raxdenstudios.commons.core"
}

versioning {
    filePath = "./libraries/$name/version.properties"
}

publishLibrary {
    name = "Android"
    description = "Android Core"
    url = "https://github.com/raxden/android-commons"
    developerId = "raxden"
    developerName = "Ángel Gómez"
    developerEmail = "raxden.dev@gmail.com"
    coordinates = Coordinates.default.copy(artifactId = "commons-core")
}

dependencies {
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.truth)
    testImplementation(libs.mockk.android)
}
