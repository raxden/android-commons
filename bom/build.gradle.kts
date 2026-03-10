import com.raxdenstudios.publishing.model.Coordinates

plugins {
    alias(libs.plugins.android.versioning)
    alias(libs.plugins.android.library.conventions)
    alias(libs.plugins.android.publish.library)
}

android {
    namespace = "com.raxdenstudios.commons.bom"
}

versioning {
    filePath = "version.properties"
}

publishLibrary {
    name = "Android"
    description = "Android library"
    url = "https://github.com/raxden/android-commons"
    developerId = "raxden"
    developerName = "Ángel Gómez"
    developerEmail = "raxden.dev@gmail.com"
    coordinates = Coordinates.default.copy(artifactId = "commons-bom")
}

dependencies {
    // If anyone use these dependencies, then are added
    constraints {
        api(projects.libraries.android)
        api(projects.libraries.androidBinding)
        api(projects.libraries.androidCompose)
        api(projects.libraries.androidTest)
        api(projects.libraries.core)
        api(projects.libraries.coroutines)
        api(projects.libraries.coroutinesTest)
        api(projects.libraries.network)
        api(projects.libraries.pagination)
        api(projects.libraries.paginationCo)
        api(projects.libraries.permissions)
        api(projects.libraries.threeten)
        api(projects.libraries.threetenTest)
    }
}
