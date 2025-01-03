import com.raxdenstudios.publishing.model.Coordinates

plugins {
    alias(libs.plugins.android.versioning)
    alias(libs.plugins.android.library.conventions)
    alias(libs.plugins.android.publish.library)
}

android {
    namespace = "com.raxdenstudios.commons.network"
}

versioning {
    filePath = "./libraries/$name/version.properties"
}

publishLibrary {
    name = "Network"
    description = "Network library"
    url = "https://github.com/raxden/android-commons"
    developerId = "raxden"
    developerName = "Ángel Gómez"
    developerEmail = "raxden.dev@gmail.com"
    coordinates = Coordinates.default.copy(artifactId = "commons-network")
}

dependencies {
    api(projects.libraries.core)

    api(libs.okhttp3)
    api(libs.okhttp3.logging.interceptor)
    api(libs.retrofit.core)
    api(libs.retrofit.gson)
    api(libs.gson)
    api(libs.network.response.adapter)

    testImplementation(libs.bundles.test)
}
