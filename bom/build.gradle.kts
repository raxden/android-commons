import com.raxdenstudios.publishing.model.Coordinates
import com.raxdenstudios.publishing.model.Developer

plugins {
    `java-platform`
    alias(libs.plugins.android.versioning)
    alias(libs.plugins.android.publish.platform)
}

versioning {
    filePath = "version.properties"
}

publishPlatform {
    name = "Android Commons BOM"
    description = "Bill of Materials for Android Commons libraries"
    url = "https://github.com/raxden/android-commons"
    developer = Developer(
        id = "raxden",
        name = "Ángel Gómez",
        email = "raxden.dev@gmail.com",
    )
    coordinates = Coordinates.default.copy(artifactId = "commons-bom")
}

javaPlatform {
    allowDependencies()
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
        api(projects.libraries.preferences)
        api(projects.libraries.threeten)
        api(projects.libraries.threetenTest)
    }
}
