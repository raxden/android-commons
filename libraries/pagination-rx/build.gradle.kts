import com.raxdenstudios.publishing.model.Coordinates

plugins {
    alias(libs.plugins.android.versioning)
    id("android-library-conventions")
    alias(libs.plugins.android.publish.library)
}

android {
    namespace = "com.raxdenstudios.commons.pagination.rx"
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
    api(projects.libraries.pagination)
    api(projects.libraries.rx)

    api(libs.rx.android)
    api(libs.rx.kotlin)

    testImplementation(projects.libraries.rxTest)
    testImplementation(libs.mockk.android)
}
