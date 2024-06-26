import com.raxdenstudios.publishing.model.Coordinates

plugins {
    alias(libs.plugins.android.versioning)
    id("android-library-conventions")
    alias(libs.plugins.android.publish.library)
}

versioning {
    filePath = "./libraries/coroutines/version.properties"
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
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.bundles.coroutines)

    implementation(projects.libraries.coroutinesTest)
    testImplementation(libs.bundles.test)
    testImplementation(libs.bundles.test.coroutines)
}
