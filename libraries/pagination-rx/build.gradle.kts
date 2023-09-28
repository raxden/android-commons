import com.raxdenstudios.publishing.model.Coordinates

plugins {
    alias(libs.plugins.android.versioning)
    id("com.raxdenstudios.android-library")
    alias(libs.plugins.android.publish.library)
}

versioning {
    filePath = "./libraries/pagination-rx/version.properties"
}

publishLibrary {
    name = "Pagination"
    description = "Pagination library"
    url = "https://github.com/raxden/android-commons"
    developerId = "raxden"
    developerName = "Ángel Gómez"
    developerEmail = "raxden.dev@gmail.com"
    coordinates = Coordinates.default.copy(artifactId = "commons-pagination-rx")
}

dependencies {
    api(project(":libraries:pagination"))
    api(project(":libraries:rx"))

    implementation(libs.bundles.rx)

    testImplementation(project(":libraries:rx-test"))
    testImplementation(libs.bundles.test)
}
