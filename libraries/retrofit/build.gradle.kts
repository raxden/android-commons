import com.raxdenstudios.publishing.model.Coordinates

plugins {
    alias(libs.plugins.android.versioning)
    id("android-library-conventions")
    alias(libs.plugins.android.publish.library)
}

android {
    namespace = "com.raxdenstudios.commons.retrofit"
}

versioning {
    filePath = "./libraries/retrofit/version.properties"
}

publishLibrary {
    name = "Retrofit Commons"
    description = "Retrofit commons is a library with a set of useful classes to help to developer " +
            "to work with retrofit."
    url = "https://github.com/raxden/android-commons"
    developerId = "raxden"
    developerName = "Ángel Gómez"
    developerEmail = "raxden.dev@gmail.com"
    coordinates = Coordinates.default.copy(artifactId = "commons-retrofit")
}

dependencies {
    api(projects.libraries.core)

    implementation(libs.bundles.retrofit2)
    implementation(libs.network.response.adapter)
    implementation(libs.network.response.adapter)

    testImplementation(libs.bundles.test)
}
