import com.raxdenstudios.publishing.model.Coordinates

plugins {
    alias(libs.plugins.android.versioning)
    id("android-library-conventions")
    alias(libs.plugins.android.publish.library)
}

android {
    namespace = "com.raxdenstudios.commons.core"
}

versioning {
    filePath = "./libraries/core/version.properties"
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
    testImplementation(libs.bundles.test)
    testImplementation(libs.bundles.test.coroutines)
}
