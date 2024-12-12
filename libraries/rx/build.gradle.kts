import com.raxdenstudios.publishing.model.Coordinates

plugins {
    alias(libs.plugins.android.versioning)
    id("android-library-conventions")
    alias(libs.plugins.android.publish.library)
}

android {
    namespace = "com.raxdenstudios.commons.rx"
}

versioning {
    filePath = "./libraries/rx/version.properties"
}

publishLibrary {
    name = "Rx Commons"
    description = "Rx commons is a library with a set of useful classes to help to developer to work with rx."
    url = "https://github.com/raxden/android-commons"
    developerId = "raxden"
    developerName = "Ángel Gómez"
    developerEmail = "raxden.dev@gmail.com"
    coordinates = Coordinates.default.copy(artifactId = "commons-rx")
}

dependencies {
    api(libs.rx.android)
    api(libs.rx.kotlin)
}
