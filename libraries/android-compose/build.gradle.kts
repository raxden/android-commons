import com.raxdenstudios.publishing.model.Coordinates

plugins {
    alias(libs.plugins.android.versioning)
    id("android-library-conventions")
    alias(libs.plugins.android.publish.library)
}

android {
    namespace = "com.raxdenstudios.commons.android.compose"
}

versioning {
    filePath = "./libraries/android-compose/version.properties"
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

    implementation(libs.android.material)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material)
}
