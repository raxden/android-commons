import com.raxdenstudios.publishing.model.Coordinates

plugins {
    alias(libs.plugins.android.versioning)
    id("com.raxdenstudios.android-library")
    alias(libs.plugins.android.publish.library)
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
    api(project(":libraries:core"))

    implementation(libs.bundles.retrofit)
    implementation(libs.network.response.adapter)

    testImplementation(libs.bundles.test)
}
