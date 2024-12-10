import com.raxdenstudios.publishing.model.Coordinates

plugins {
    alias(libs.plugins.android.versioning)
    id("android-library-conventions")
    alias(libs.plugins.android.publish.library)
}

android {
    namespace = "com.raxdenstudios.commons.android.binding"
    buildFeatures.viewBinding = true
}

versioning {
    filePath = "./libraries/android-binding/version.properties"
}

publishLibrary {
    name = "Android"
    description = "Android library"
    url = "https://github.com/raxden/android-commons"
    developerId = "raxden"
    developerName = "Ángel Gómez"
    developerEmail = "raxden.dev@gmail.com"
    coordinates = Coordinates.default.copy(artifactId = "commons-android-binding")
}

dependencies {
    api(projects.libraries.core)
    api(projects.libraries.android)

    implementation(libs.android.material)

    testImplementation(libs.bundles.test)
}
