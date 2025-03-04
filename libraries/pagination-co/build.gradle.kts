import com.raxdenstudios.publishing.model.Coordinates

plugins {
    alias(libs.plugins.android.versioning)
    alias(libs.plugins.android.library.conventions)
    alias(libs.plugins.android.publish.library)
}

android {
    namespace = "com.raxdenstudios.commons.pagination.co"
}

versioning {
    filePath = "./libraries/$name/version.properties"
}

publishLibrary {
    name = "Pagination"
    description = "Pagination library"
    url = "https://github.com/raxden/android-commons"
    developerId = "raxden"
    developerName = "Ángel Gómez"
    developerEmail = "raxden.dev@gmail.com"
    coordinates = Coordinates.default.copy(artifactId = "commons-pagination-co")
}

dependencies {
    api(projects.libraries.pagination)
    api(projects.libraries.coroutines)

    testImplementation(projects.libraries.coroutinesTest)
    testImplementation(libs.mockk.android)
}
