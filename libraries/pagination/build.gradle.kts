import com.raxdenstudios.publishing.model.Coordinates
import extension.implementationBundle

plugins {
    alias(libs.plugins.android.versioning)
    id("android-library-conventions")
    alias(libs.plugins.android.publish.library)
}

android {
    namespace = "com.raxdenstudios.commons.pagination"
}

versioning {
    filePath = "./libraries/pagination/version.properties"
}

publishLibrary {
    name = "Pagination"
    description = "Pagination library"
    url = "https://github.com/raxden/android-commons"
    developerId = "raxden"
    developerName = "Ángel Gómez"
    developerEmail = "raxden.dev@gmail.com"
    coordinates = Coordinates.default.copy(artifactId = "commons-pagination")
}

dependencies {
    implementationBundle(libs.bundles.android.material)
    implementationBundle(libs.bundles.androidx.compose)

    testImplementation(libs.bundles.test)
}
