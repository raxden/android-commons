import com.raxdenstudios.publishing.model.Coordinates

plugins {
    alias(libs.plugins.android.versioning)
    id("android-library-conventions")
    alias(libs.plugins.android.publish.library)
}

android {
    namespace = "com.raxdenstudios.commons.coroutines"
}

versioning {
    filePath = "./libraries/$name/version.properties"
}

publishLibrary {
    name = "Coroutines"
    description = "Coroutines library"
    url = "https://github.com/raxden/android-commons"
    developerId = "raxden"
    developerName = "Ángel Gómez"
    developerEmail = "raxden.dev@gmail.com"
    coordinates = Coordinates.default.copy(artifactId = "commons-coroutines")
}

dependencies {
    api(projects.libraries.core)

    api(libs.coroutines.android)

    testImplementation(projects.libraries.coroutinesTest)
    testImplementation(libs.truth)
    testImplementation(libs.mockk.android)
}
