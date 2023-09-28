import com.raxdenstudios.publishing.model.Coordinates

plugins {
    alias(libs.plugins.android.versioning)
    id("com.raxdenstudios.android-library")
    alias(libs.plugins.android.publish.library)
}

versioning {
    filePath = "./libraries/coroutines-test/version.properties"
}

publishLibrary {
    name = "Coroutines"
    description = "Coroutines library"
    url = "https://github.com/raxden/android-commons"
    developerId = "raxden"
    developerName = "Ángel Gómez"
    developerEmail = "raxden.dev@gmail.com"
    coordinates = Coordinates.default.copy(artifactId = "commons-coroutines-test")
}

dependencies {
    implementation(libs.bundles.test)
    implementation(libs.bundles.test.coroutines)
}
