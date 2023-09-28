import com.raxdenstudios.publishing.model.Coordinates

plugins {
    alias(libs.plugins.android.versioning)
    id("com.raxdenstudios.android-library")
    alias(libs.plugins.android.publish.library)
}

versioning {
    filePath = "./libraries/rx-test/version.properties"
}

publishLibrary {
    name = "Rx Test Commons"
    description = "Rx Test commons is a library with a set of useful classes to help to developer to work with Rx."
    url = "https://github.com/raxden/android-commons"
    developerId = "raxden"
    developerName = "Ángel Gómez"
    developerEmail = "raxden.dev@gmail.com"
    coordinates = Coordinates.default.copy(artifactId = "commons-rx-test")
}

dependencies {
    implementation(libs.bundles.rx)
    implementation(libs.bundles.test)
}
