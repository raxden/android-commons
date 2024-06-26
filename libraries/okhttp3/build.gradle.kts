import com.raxdenstudios.publishing.model.Coordinates

plugins {
    alias(libs.plugins.android.versioning)
    id("android-library-conventions")
    alias(libs.plugins.android.publish.library)
}

versioning {
    filePath = "./libraries/okhttp3/version.properties"
}

publishLibrary {
    name = "Okhttp3"
    description = "Okhttp3 library"
    url = "https://github.com/raxden/android-commons"
    developerId = "raxden"
    developerName = "Ángel Gómez"
    developerEmail = "raxden.dev@gmail.com"
    coordinates = Coordinates.default.copy(artifactId = "commons-okhttp3")
}

dependencies {
    implementation(libs.bundles.okhttp3)

    testImplementation(libs.bundles.test)
}
