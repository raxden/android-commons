import com.raxdenstudios.publishing.model.Coordinates

plugins {
    alias(libs.plugins.android.versioning)
    id("android-library-conventions")
    alias(libs.plugins.android.publish.library)
}

android {
    namespace = "com.raxdenstudios.commons.threeten"
}

versioning {
    filePath = "./libraries/threeten/version.properties"
}

publishLibrary {
    name = "Threeten Commons"
    description = "Threeten commons is a library with a set of useful classes to help to developer " +
            "to work with threeten."
    url = "https://github.com/raxden/android-commons"
    developerId = "raxden"
    developerName = "Ángel Gómez"
    developerEmail = "raxden.dev@gmail.com"
    coordinates = Coordinates.default.copy(artifactId = "commons-threeten")
}

dependencies {
    implementation(libs.threetenabp)

    testImplementation(projects.libraries.threetenTest)
    testImplementation(libs.bundles.test)
}
