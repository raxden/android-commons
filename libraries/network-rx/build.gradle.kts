import com.raxdenstudios.publishing.model.Coordinates

plugins {
    alias(libs.plugins.android.versioning)
    alias(libs.plugins.android.library.conventions)
    alias(libs.plugins.android.publish.library)
}

android {
    namespace = "com.raxdenstudios.commons.network.rx"
}

versioning {
    filePath = "./libraries/$name/version.properties"
}

publishLibrary {
    name = "Network Commons"
    description = "Network commons is a library with a set of useful classes to help to developer " +
            "to work with retrofit."
    url = "https://github.com/raxden/android-commons"
    developerId = "raxden"
    developerName = "Ángel Gómez"
    developerEmail = "raxden.dev@gmail.com"
    coordinates = Coordinates.default.copy(artifactId = "commons-network-rx")
}

dependencies {
    api(projects.libraries.network)

    api(libs.retrofit.core)
    api(libs.retrofit.rxjava2)

    testImplementation(libs.bundles.test)
}
