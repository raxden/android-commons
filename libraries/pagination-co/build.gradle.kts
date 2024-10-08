import com.raxdenstudios.publishing.model.Coordinates
import extension.implementationBundle

plugins {
    alias(libs.plugins.android.versioning)
    id("android-library-conventions")
    alias(libs.plugins.android.publish.library)
}

android {
    namespace = "com.raxdenstudios.commons.pagination.co"
}

versioning {
    filePath = "./libraries/pagination-co/version.properties"
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

    implementationBundle(libs.bundles.coroutines)

    testImplementation(projects.libraries.coroutinesTest)
    testImplementation(libs.bundles.test)
    testImplementation(libs.bundles.test.coroutines)
}
