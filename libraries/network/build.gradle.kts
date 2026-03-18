import com.raxdenstudios.publishing.model.Coordinates
import com.raxdenstudios.publishing.model.Developer

plugins {
    alias(libs.plugins.android.versioning)
    alias(libs.plugins.android.library.conventions)
    alias(libs.plugins.android.publish.library)
}

android {
    namespace = "com.raxdenstudios.commons.network"
}

versioning {
    filePath = "version.properties"
}

publishLibrary {
    name = "Network"
    description = "Network library"
    url = "https://github.com/raxden/android-commons"
    developer = Developer(
        id = "raxden",
        name = "Ángel Gómez",
        email = "raxden.dev@gmail.com",
    )
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
