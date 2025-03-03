import com.raxdenstudios.publishing.model.Coordinates

plugins {
    alias(libs.plugins.android.versioning)
    alias(libs.plugins.android.compose.library.conventions)
    alias(libs.plugins.android.publish.library)
}

android {
    namespace = "com.raxdenstudios.commons.android.compose"
}

versioning {
    filePath = "./libraries/$name/version.properties"
}

publishLibrary {
    name = "Android Compose"
    description = "Android compose library"
    url = "https://github.com/raxden/android-commons"
    developerId = "raxden"
    developerName = "Ángel Gómez"
    developerEmail = "raxden.dev@gmail.com"
    coordinates = Coordinates.default.copy(artifactId = "commons-android-compose")
}

dependencies {
    api(projects.libraries.android)

    api(platform(libs.androidx.compose.bom))
    api(libs.androidx.compose.material)
}
